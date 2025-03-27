

package com.mylog.common.utils.resultutils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author pss
 * @date 2022年1月7日
 */
public class R<T> extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static <T> R<T> error() {
        return error(ErrorCode.OPERATION_ERROR.getCode(), ErrorCode.OPERATION_ERROR.getMessage());
    }

    public static <T> R<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> R<T> error(ErrorCode errorCode, String message) {
        return error(errorCode.getCode(), message);
    }

    public static <T> R<T> error(String msg) {
        return error(ErrorCode.OPERATION_ERROR.getCode(), msg);
    }

    public static <T> R<T> error(int code) {
        return error(code, ErrorCode.OPERATION_ERROR.getMessage());
    }


    public static <T> R<T> error(int code, String msg) {
        R r = new R<T>();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static <T> R<T> ok(String msg) {
        R r = new R<T>();
        r.put("data", msg);
        return r;
    }

    public static <T> R<T> ok(Map<String, Object> map) {
        R r = new R<T>();
        r.putAll(map);
        r.put("code", 0);
        r.put("msg", "success");
        return r;
    }

    public static <T> R<T> ok() {
        return new R<T>();
    }

    public static <T> R<T> ok(Object value) {
        R r = new R<T>();
        r.put("data", value);
        return r;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
