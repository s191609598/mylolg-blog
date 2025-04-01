package com.mylog.framework.manager;

import com.mylog.common.annotation.OpLog;
import com.mylog.common.utils.ServletUtils;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.framework.factory.LogFactory;
import com.mylog.framework.factory.LogTaskFactory;
import com.mylog.system.entity.SysOpLog;
import org.aspectj.lang.JoinPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;

import javax.servlet.http.HttpServletRequest;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author pss
 * @date 2025/3/29
 */
public class LogManager {
    /**
     * 异步操作记录日志的线程池
     */
    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(10, new ScheduledExecutorFactoryBean());

    private LogManager() {
    }

    private static final LogManager LOG_MANAGER = new LogManager();

    public static LogManager me() {
        return LOG_MANAGER;
    }

    /**
     * 异步执行日志的方法
     */
    private void executeLog(TimerTask task) {
        //日志记录操作延时
        int operateDelayTime = 10;
        EXECUTOR.schedule(task, operateDelayTime, TimeUnit.MILLISECONDS);
    }


    /**
     * 操作日志
     */
    public void executeOperationLog(OpLog opLog, Long userId, JoinPoint joinPoint, String result, Long costTime) {
        SysOpLog sysOpLog = this.genBaseSysOpLog(opLog);
        TimerTask timerTask = LogTaskFactory.operationLog(sysOpLog, userId, opLog, joinPoint, result, costTime);
        executeLog(timerTask);
    }

    /**
     * 异常日志
     *
     * @param opLog
     * @param userId
     * @param joinPoint
     * @param exception
     */
    public void executeExceptionLog(OpLog opLog, Long userId, JoinPoint joinPoint, Exception exception, Long costTime) {
        SysOpLog sysOpLog = this.genBaseSysOpLog(opLog);
        TimerTask timerTask = LogTaskFactory.exceptionLog(sysOpLog, userId, opLog, joinPoint, exception, costTime);
        executeLog(timerTask);
    }

    /**
     * 构建基础操作日志
     *
     * @return
     */
    private SysOpLog genBaseSysOpLog(OpLog opLog) {
        HttpServletRequest request = ServletUtils.getRequest();
        if (StringUtils.isNotNull(request)) {
            String ip = IpUtils.getIp(request);
            String address = IpUtils.getAddress(request);
            String url = request.getRequestURI();
            String method = request.getMethod();
            String useragent = request.getHeader(HttpHeaders.USER_AGENT);
            return LogFactory.genSysOpLog(ip, address, url, method, useragent);
        } else {
            String ip = "127.0.0.1";
            String address = "127.0.0.1";
            String url = "url为空";
            String method = opLog.title();
            String useragent = "User-Agent";
            return LogFactory.genSysOpLog(ip, address, url, method, useragent);
        }
    }


}
