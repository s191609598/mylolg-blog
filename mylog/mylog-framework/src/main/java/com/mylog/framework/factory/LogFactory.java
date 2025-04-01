package com.mylog.framework.factory;

import cn.hutool.core.date.DateTime;
import com.mylog.common.annotation.OpLog;
import com.mylog.common.utils.JoinPointUtil;
import com.mylog.system.entity.SysOpLog;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;

/**
 * 日志对象创建工厂
 *
 * @author pss
 * @date 2025/3/29
 */
public class LogFactory {

    /**
     * 创建操作日志
     *
     * @param sysOpLog
     * @param userId
     * @param opLog
     * @param joinPoint
     * @param result
     */
    static void createSysOperationLog(SysOpLog sysOpLog, Long userId, OpLog opLog, JoinPoint joinPoint, String result, Long requestTime) {
        fillCommonSysOpLog(sysOpLog, userId, opLog, joinPoint, requestTime);
        sysOpLog.setStatus(1);
        sysOpLog.setJsonResult(result);
        sysOpLog.setRequestTime(requestTime);
        sysOpLog.setOperation(opLog.title());
//        String param = JoinPointUtil.getArgsJsonString(joinPoint);
//        sysOpLog.setRequestParams(param);
//        sysOpLog.setBusinessType(opLog.businessType().ordinal());
//        sysOpLog.setCreateBy(userId);
//        sysOpLog.setCreateDate(DateTime.now());
    }

    /**
     * 创建异常日志
     *
     * @param sysOpLog
     * @param userId
     * @param opLog
     * @param joinPoint
     * @param exception
     */
    static void createSysExceptionLog(SysOpLog sysOpLog, Long userId, OpLog opLog, JoinPoint joinPoint, Exception exception, Long requestTime) {
        fillCommonSysOpLog(sysOpLog, userId, opLog, joinPoint, requestTime);
        sysOpLog.setStatus(0);
        sysOpLog.setResultError(exception.getMessage());
        sysOpLog.setStackTrace(Arrays.toString(exception.getStackTrace()));
//        String param = JoinPointUtil.getArgsJsonString(joinPoint);
//        sysOpLog.setRequestTime(requestTime);
//        sysOpLog.setOperation(opLog.title());
//        sysOpLog.setRequestParams(param);
//        sysOpLog.setBusinessType(opLog.businessType().ordinal());
//        sysOpLog.setCreateBy(userId);
//        sysOpLog.setCreateDate(DateTime.now());
    }


    private static void fillCommonSysOpLog(SysOpLog sysOpLog, Long userId, OpLog opLog, JoinPoint joinPoint, Long requestTime) {
        String param = JoinPointUtil.getArgsJsonString(joinPoint);
        sysOpLog.setRequestTime(requestTime);
        sysOpLog.setOperation(opLog.title());
        sysOpLog.setRequestParams(param);
        sysOpLog.setBusinessType(opLog.businessType().ordinal());
        sysOpLog.setCreateBy(userId);
        sysOpLog.setCreateDate(DateTime.now());
    }


    public static SysOpLog genSysOpLog(String ip, String location, String url, String method,String userAgent) {
        SysOpLog sysOpLog = new SysOpLog();
        sysOpLog.setIp(ip);
        sysOpLog.setOperLocation(location);
        sysOpLog.setRequestUri(url);
        sysOpLog.setRequestMethod(method);
        sysOpLog.setUserAgent(userAgent);
        return sysOpLog;
    }
}
