package com.mylog.common.enums;

/**
 *
 * 异常信息类型
 * @author Pss
 * @date 2022/1/11 15:43
 */
public interface ErrorType {

    /**
     * 请求方式的异常
     */
    int REQUEST_MODE_ERROR = 0;
    /**
     * 业务异常
     */
    int BUSINESS_ERROR = 1;
    /**
     * 未知运行时异常
     */
    int UNKNOWN_RUN_ERROR = 2;
    /**
     * 系统异常
     */
    int SYSTEM_ERROR = 3;
    /**
     * token异常
     */
    int TOKEN_ERROR = 4;
}
