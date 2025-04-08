

package com.mylog.common.exception;


import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.resultutils.ErrorCode;

import java.io.Serializable;

/**
 * 自定义异常
 */
public class MyException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;

//    public MyException(int code) {
//        this.code = code;
//        this.msg = MessageUtils.getMessage(code);
//    }

    public MyException() {
        this.code = ErrorCode.SYSTEM_ERROR.getCode();
        this.msg = ErrorCode.SYSTEM_ERROR.getMessage();
    }

    public MyException(int code, String... params) {
        this.code = code;
        this.msg = StringUtils.join(params, ",");
    }

    public MyException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
    }

    public MyException(ErrorCode errorCode, String msg) {
        this.code = errorCode.getCode();
        this.msg = msg;
    }

    public MyException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.msg = e.getMessage();
    }

    public MyException(Throwable e) {
        super(e);
        this.code = ErrorCode.SYSTEM_ERROR.getCode();
        ;
        this.msg = e.getMessage();
    }

    public MyException(int code, Throwable e, String... params) {
        super(e);
        this.code = code;
        this.msg = StringUtils.join(params, ",");
    }

    public MyException(String msg) {
        super(msg);
        this.code = ErrorCode.SYSTEM_ERROR.getCode();
        this.msg = msg;
    }

    public MyException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.SYSTEM_ERROR.getCode();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}