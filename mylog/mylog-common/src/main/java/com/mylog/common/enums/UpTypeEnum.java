package com.mylog.common.enums;

import lombok.Getter;

/**
 * 点赞类型
 *
 * @author pss
 */
@Getter
public enum UpTypeEnum {
    // 点赞
    INCR(1),
    // 取消点赞
    DECR(-1),
    // 不发生改变
    NON(0),
    ;

    private final int value;

    UpTypeEnum(int value) {
        this.value = value;
    }

}
