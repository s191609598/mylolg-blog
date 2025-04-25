package com.mylog.common.cache;

import lombok.Data;

/**
 * @author pss
 * @date 2025/4/22
 */
@Data
public class Item {
    private String key;
    private Integer count;

    public Item(String key, Integer count) {
        this.key = key;
        this.count = count;
    }
}
