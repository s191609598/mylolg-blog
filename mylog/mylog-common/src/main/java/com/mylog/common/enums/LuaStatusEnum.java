package com.mylog.common.enums;

import lombok.Getter;

/**
 * @author pss
 * @date 2025/4/24
 */
@Getter
public enum LuaStatusEnum {
    // 成功
    SUCCESS(1L),
    // 失败
    FAIL(-1L),
    ;

    private final long value;

    LuaStatusEnum(long value) {
        this.value = value;
    }

}
