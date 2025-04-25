package com.mylog.common.cache;


import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author pss
 * @date 2025/4/22
 */
public interface TopK {

    AddResult add(String key, int increment);

    List<Item> list();

    BlockingQueue<Item> expelled();

    void fading();

    long total();
}
