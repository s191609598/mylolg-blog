package com.mylog.common.enums;

import com.mylog.common.utils.StringUtils;
import lombok.Getter;

/**
 * @author pss
 * @date 2025/2/12
 */
@Getter
public enum UserRoleEnum {
    ADMIN("admin", "管理员"), USER("user", "普通用户"), BAN("ban", "非法用户"),
    ;

    private String text;
    private String value;

    UserRoleEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public static UserRoleEnum getEnum(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.getValue().equals(value)) {
                return userRoleEnum;
            }
        }
        return null;
    }
}
