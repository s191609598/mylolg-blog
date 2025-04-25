package com.mylog.common.cache;

import cn.hutool.core.util.HashUtil;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author pss
 * @date 2025/4/22
 */
public class HeavyKeeper implements TopK {
    private static final int LOOKUP_TABLE_SIZE = 256;
    private final int k;
    private final int width;
    private final int depth;
    private final double[] lookupTable;
    private final Bucket[][] buckets;
    private final PriorityQueue<Node> minHeap;
    private final BlockingQueue<Item> expelledQueue;
    private final Random random;
    private long total;
    private final int minCount;


    public HeavyKeeper(int k, int width, int depth, double decay, int minCount) {
        this.k = k;
        this.width = width;
        this.depth = depth;
        this.minCount = minCount;

        this.lookupTable = new double[LOOKUP_TABLE_SIZE];
        for (int i = 0; i < LOOKUP_TABLE_SIZE; i++) {
            lookupTable[i] = Math.pow(decay, i);
        }

        this.buckets = new Bucket[depth][width];
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                buckets[i][j] = new Bucket();
            }
        }

        this.minHeap = new PriorityQueue<>(Comparator.comparingInt(n -> n.count));
        this.expelledQueue = new LinkedBlockingQueue<>();
        this.random = new Random();
        this.total = 0;
    }

    /**
     * 核心方法，主要作用是添加元素并更新Topk结构
     * @param key
     * @param increment
     * @return
     */
    @Override
    public AddResult add(String key, int increment) {
        byte[] keyBytes = key.getBytes();
        long itemFingerprint = hash(keyBytes);
        int maxCount = 0;

        for (int i = 0; i < depth; i++) {
            int bucketNumber = Math.abs(hash(keyBytes)) % width;
            Bucket bucket = buckets[i][bucketNumber];

            synchronized (bucket) {
                if (bucket.count == 0) {
                    bucket.fingerprint = itemFingerprint;
                    bucket.count = increment;
                    maxCount = Math.max(maxCount, increment);
                } else if (bucket.fingerprint == itemFingerprint) {
                    bucket.count += increment;
                    maxCount = Math.max(maxCount, bucket.count);
                } else {
                    for (int j = 0; j < increment; j++) {
                        double decay = bucket.count < LOOKUP_TABLE_SIZE ? lookupTable[bucket.count] : lookupTable[LOOKUP_TABLE_SIZE - 1];
                        if (random.nextDouble() < decay) {
                            bucket.count--;
                            if (bucket.count == 0) {
                                bucket.fingerprint = itemFingerprint;
                                bucket.count = increment - j;
                                maxCount = Math.max(maxCount, bucket.count);
                                break;
                            }
                        }
                    }
                }
            }
        }

        total += increment;

        if (maxCount < minCount) {
            return new AddResult(null, false, null);
        }

        synchronized (minHeap) {
            boolean isHot = false;
            String expelled = null;

            Optional<Node> existing = minHeap.stream().filter(n -> n.key.equals(key)).findFirst();

            if (existing.isPresent()) {
                minHeap.remove(existing.get());
                minHeap.add(new Node(key, maxCount));
                isHot = true;
            } else {
                if (minHeap.size() < k || maxCount >= Objects.requireNonNull(minHeap.peek()).count) {
                    Node newNode = new Node(key, maxCount);
                    if (minHeap.size() >= k) {
                        expelled = minHeap.poll().key;
                        expelledQueue.offer(new Item(expelled, maxCount));
                    }
                    minHeap.add(newNode);
                    isHot = true;
                }
            }

            return new AddResult(expelled, isHot, key);
        }
    }

    /**
     * 返回当前TopK元素的列表
     * @return
     */
    @Override
    public List<Item> list() {
        synchronized (minHeap) {
            List<Item> result = new ArrayList<>(minHeap.size());
            for (Node node : minHeap) {
                result.add(new Item(node.key, node.count));
            }
            result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
            return result;
        }
    }

    /**
     * 获取被淘汰的元素列表
     * 注意：该方法不会删除被淘汰的元素，而是将其放入一个队列中，以便后续处理。
     * @return
     */
    @Override
    public BlockingQueue<Item> expelled() {
        return expelledQueue;
    }

    /**
     * 衰减所有元素的计数
     * 注意：该方法会将所有元素的计数减半，以模拟元素的衰减过程。
     * 衰减后，元素的计数可能会小于minCount，此时该元素将被视为非热门元素。
     * 衰减后的元素将被放入expelled队列中，以便后续处理。
     */
    @Override
    public void fading() {
        for (Bucket[] row : buckets) {
            for (Bucket bucket : row) {
                synchronized (bucket) {
                    bucket.count = bucket.count >> 1;
                }
            }
        }

        synchronized (minHeap) {
            PriorityQueue<Node> newHeap = new PriorityQueue<>(Comparator.comparingInt(n -> n.count));
            for (Node node : minHeap) {
                newHeap.add(new Node(node.key, node.count >> 1));
            }
            minHeap.clear();
            minHeap.addAll(newHeap);
        }

        total = total >> 1;
    }

    @Override
    public long total() {
        return total;
    }

    private static class Bucket {
        long fingerprint;
        int count;
    }

    private static class Node {
        final String key;
        final int count;

        Node(String key, int count) {
            this.key = key;
            this.count = count;
        }
    }

    private static int hash(byte[] data) {
        return HashUtil.murmur32(data);
    }
}
