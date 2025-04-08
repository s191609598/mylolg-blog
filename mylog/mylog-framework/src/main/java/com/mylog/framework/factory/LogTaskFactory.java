package com.mylog.framework.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.mylog.common.annotation.OpLog;
import com.mylog.system.entity.SysOpLog;
import com.mylog.system.service.SysOpLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import java.util.TimerTask;

/**
 * @author pss
 * @date 2025/3/29
 */
@Slf4j
public class LogTaskFactory {


    private static final SysOpLogService sysOpLogService = SpringUtil.getBean(SysOpLogService.class);

    /**
     * 操作日志
     *
     * @param sysOpLog
     * @param userId
     * @param opLog
     * @param joinPoint
     * @param result
     * @return
     */
    public static TimerTask operationLog(SysOpLog sysOpLog, Long userId, OpLog opLog, JoinPoint joinPoint, String result, Long costTime) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysOperationLog(sysOpLog, userId, opLog, joinPoint, result, costTime);
                    sysOpLogService.save(sysOpLog);
                } catch (Exception e) {
                    log.error(">>> 创建操作日志异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
                }
            }
        };
    }

    /**
     * 异常日志
     *
     * @param sysOpLog
     * @param userId
     * @param opLog
     * @param joinPoint
     * @param exception
     * @return
     */
    public static TimerTask exceptionLog(SysOpLog sysOpLog, Long userId, OpLog opLog, JoinPoint joinPoint, Exception exception, Long costTime) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysExceptionLog(sysOpLog, userId, opLog, joinPoint, exception, costTime);
                    sysOpLogService.save(sysOpLog);
                } catch (Exception e) {
                    log.error(">>> 创建异常日志异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
                }
            }
        };
    }
}
