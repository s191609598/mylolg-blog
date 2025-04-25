//package com.mylog.common.cache;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import com.mylog.common.utils.redis.RedisCacheUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author pss
// * @date 2025/4/22
// */
//@Component
//@Slf4j
//public class CacheManager {
//    private TopK hotKeyDetector;
//    private Cache<String, Object> localCache;
//
//    @Bean
//    public TopK getHotKeyDetector() {
//        hotKeyDetector = new HeavyKeeper(
//                // 监控 Top 100 Key
//                100,
//                // 宽度
//                100000,
//                // 深度
//                5,
//                // 衰减系数
//                0.92,
//                // 最小出现 10 次才记录
//                10);
//        return hotKeyDetector;
//    }
//
//    @Bean
//    public Cache<String, Object> localCache() {
//        return localCache = Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(5, TimeUnit.MINUTES).build();
//    }
//
//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Resource
//    private RedisCacheUtils redisCacheUtils;
//
//    /**
//     * 获取数据
//     *
//     * @param key
//     * @return
//     */
//    public Object get(String key) {
//
//        // 1. 先查本地缓存
//        Object value = localCache.getIfPresent(key);
//        if (value != null) {
//            log.info("本地缓存获取到数据 {} = {}", key, value);
//            // 记录访问次数（每次访问计数 +1）
//            hotKeyDetector.add(key, 1);
//            return value;
//        }
//
//        // 2. 本地缓存未命中，查询 Redis
////        Object redisValue = redisTemplate.opsForHash().get(key, key);
//        Object redisValue = redisCacheUtils.getCacheObject(key);
//        if (redisValue == null) {
//            return null;
//        }
//
//        // 3. 记录访问（计数 +1）
//        AddResult addResult = hotKeyDetector.add(key, 1);
//
//        // 4. 如果是热 Key 且不在本地缓存，则缓存数据
//        if (addResult.isHotKey()) {
//            localCache.put(key, redisValue);
//        }
//
//        return redisValue;
//    }
//
//    public void putIfPresent(String key, Object value) {
//        Object object = localCache.getIfPresent(key);
//        if (object == null) {
//            return;
//        }
//        localCache.put(key, value);
//    }
//
//    // 定时清理过期的热 Key 检测数据
//    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS)
//    public void cleanHotKeys() {
//        hotKeyDetector.fading();
//    }
//
//}
