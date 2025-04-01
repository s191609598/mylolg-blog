package com.mylog.framework.satoken;

/**
 * @author pss
 * @date 2025/2/6 23:28
 */

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.mylog.common.enums.BusinessType;
import com.mylog.common.enums.ErrorType;
import com.mylog.common.exception.ExceptionUtils;
import com.mylog.common.exception.MyException;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.utils.resultutils.R;
import com.mylog.framework.async.AsyncDao;
import com.mylog.framework.async.AsyncFactory;
import com.mylog.system.entity.SysErrorLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: pss
 * @CreateTime: 2025-02-06
 * @Version: 1.0
 * sa-token全局异常拦截
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截
    @ExceptionHandler
    public R handlerException(Exception e, HttpServletRequest request) {
        this.saveLog(ErrorType.UNKNOWN_RUN_ERROR, request, e);
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常'{}'", requestURI, e.getMessage());
//        e.printStackTrace();
        return R.error(e.getMessage());
    }

    @ExceptionHandler({MyException.class})
    public R handlerMyException(MyException e, HttpServletRequest request) {
        this.saveLog(ErrorType.UNKNOWN_RUN_ERROR, request, e);
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常'{}'", requestURI, e.getMessage());
//        e.printStackTrace();
        return R.error(e.getCode(), e.getMsg());
    }

    /**
     * 未登录异常拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler({NotLoginException.class})
    public R handlerNotLoginException(Exception e, HttpServletRequest request) {
        this.saveLog(ErrorType.TOKEN_ERROR, request, e);
        log.error("请求地址'{}',发生未登录异常'{}'", request.getRequestURI(), e.getMessage());
//        e.printStackTrace();
        return R.error(ErrorCode.NOT_LOGIN_ERROR);
    }

    /**
     * 记录异常日志
     *
     * @param errorType 异常类型 0=请求方式异常 1=业务异常 2=未知运行时异常 3=系统异常 4=token异常
     * @param request
     * @param e
     */
    private void saveLog(Integer errorType, HttpServletRequest request, Exception e) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        SysErrorLog errorLog = new SysErrorLog();
        errorLog.setRequestUri(requestURI);
        errorLog.setRequestMethod(method);
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        if (StringUtils.isNotNull(loginIdDefaultNull)) {
            errorLog.setCreateBy(Long.valueOf(loginIdDefaultNull.toString()));
        }
        errorLog.setRequestParams(JSON.toJSONString(request.getParameterMap()));
        errorLog.setBusinessType(BusinessType.OTHER.ordinal());
        errorLog.setIp(IpUtils.getIp(request));
        errorLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        errorLog.setErrorInfo(ExceptionUtils.getErrorStackTrace(e));
        errorLog.setErrorType(errorType);
        AsyncDao.me().execute(AsyncFactory.recordErrorLog(errorLog));
    }

}
