package com.mylog.common.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.mylog.common.exception.MyException;
import com.mylog.common.utils.resultutils.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 校验工具类
 *
 * @since 1.0.0
 */
public class AssertUtils {

    public static void isBlank(String str, String... params) {
        isBlank(str, ErrorCode.PARAMS_ERROR.getCode(), params);
    }

    public static void isBlank(String str, ErrorCode errorCode) {
        isBlank(str, errorCode.getCode(), errorCode.getMessage());
    }

    public static void isBlank(Object str, String... params) {
        isNull(str, ErrorCode.PARAMS_ERROR.getCode(), params);
        isBlank(str.toString(), ErrorCode.PARAMS_ERROR.getCode(), params);
    }

    public static void isBlank(Object str, ErrorCode errorCode) {
        isBlank(str.toString(), errorCode.getCode(), errorCode.getMessage());
    }

    public static void isBlank(String str, Integer code, String... params) {
        if (code == null) {
            throw new MyException(ErrorCode.PARAMS_ERROR.getCode(), "code");
        }
        if (StringUtils.isBlank(str)) {
            throw new MyException(code, params);
        }
    }

    public static void isNull(Object object, String... params) {
        isNull(object, ErrorCode.PARAMS_ERROR.getCode(), params);
    }

    public static void isNull(Object object, ErrorCode errorCode) {
        isNull(object, errorCode.getCode(), errorCode.getMessage());
    }

    public static void isNull(Object object, ErrorCode errorCode,String params) {
        isNull(object, errorCode.getCode(), errorCode.getMessage(),params);
    }

    public static void isNull(Object object, Integer code, String... params) {
        if (code == null) {
            throw new MyException(ErrorCode.PARAMS_ERROR.getCode(), "code");
        }

        if (object == null || Objects.isNull(object)) {
            throw new MyException(code, params);
        }
    }

    public static void isArrayEmpty(Object[] array, String... params) {
        isArrayEmpty(array, ErrorCode.PARAMS_ERROR.getCode(), params);
    }

    public static void isArrayEmpty(Object[] array, ErrorCode errorCode) {
        isArrayEmpty(array, errorCode.getCode(), errorCode.getMessage());
    }


    public static void isArrayEmpty(Object[] array, Integer code, String... params) {
        if (code == null) {
            throw new MyException(ErrorCode.PARAMS_ERROR.getCode(), "code");
        }

        if (ArrayUtil.isEmpty(array)) {
            throw new MyException(code, params);
        }
    }

    public static void isListEmpty(List<?> list, String... params) {
        isListEmpty(list, ErrorCode.PARAMS_ERROR.getCode(), params);
    }

    public static void isListEmpty(List<?> list, ErrorCode errorCode) {
        isListEmpty(list, errorCode.getCode(), errorCode.getMessage());
    }


    public static void isListEmpty(List<?> list, Integer code, String... params) {
        if (code == null) {
            throw new MyException(ErrorCode.PARAMS_ERROR.getCode(), "code");
        }

        if (CollUtil.isEmpty(list)) {
            throw new MyException(code, params);
        }
    }

    public static void isMapEmpty(Map map, String... params) {
        isMapEmpty(map, ErrorCode.PARAMS_ERROR.getCode(), params);
    }

    public static void isMapEmpty(Map map, ErrorCode errorCode) {
        isMapEmpty(map, errorCode.getCode(), errorCode.getMessage());
    }

    public static void isMapEmpty(Map map, Integer code, String... params) {
        if (code == null) {
            throw new MyException(ErrorCode.PARAMS_ERROR.getCode(), "code");
        }

        if (MapUtil.isEmpty(map)) {
            throw new MyException(code, params);
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param myException
     */
    public static void assertIf(boolean condition, MyException myException) {
        if (condition) {
            throw myException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void assertIf(boolean condition, ErrorCode errorCode) {
        assertIf(condition, new MyException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param message
     */
    public static void assertIf(boolean condition, String message) {
        assertIf(condition, new MyException(message));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void assertIf(boolean condition, ErrorCode errorCode, String message) {
        assertIf(condition, new MyException(errorCode, message));
    }

}