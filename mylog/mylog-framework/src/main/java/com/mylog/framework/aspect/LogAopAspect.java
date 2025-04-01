package com.mylog.framework.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.mylog.common.annotation.OpLog;
import com.mylog.common.utils.StringUtils;
import com.mylog.framework.manager.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 操作日志切面
 *
 * @author pss
 * @date 2025/3/29
 */
@Aspect
@Component
public class LogAopAspect {

    private static final ThreadLocal<Long> TIME_HOLDER = new ThreadLocal<>();

    @Pointcut("@annotation(com.mylog.common.annotation.OpLog)")
    private void getLogPointCut() {
    }

    /**
     * 操作成功返回结果记录日志
     *
     * @param joinPoint
     * @param result
     */
    @Async
    @AfterReturning(pointcut = "getLogPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        OpLog opLog = method.getAnnotation(OpLog.class);
        Long userId = null;
        try {
            Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
            if (StringUtils.isNotNull(loginIdDefaultNull)) {
                userId = Long.valueOf(loginIdDefaultNull.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //记录请求耗时
        Long costTime = System.currentTimeMillis() - TIME_HOLDER.get();
        //异步记录日志
        LogManager.me().executeOperationLog(opLog, userId, joinPoint, JSON.toJSONString(result), costTime);
    }

    /**
     * 操作发生异常记录日志
     *
     * @param joinPoint
     * @param exception
     */
    @Async
    @AfterThrowing(pointcut = "getLogPointCut()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exception) {
        Long userId = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        OpLog opLog = method.getAnnotation(OpLog.class);
        try {
            Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
            if (StringUtils.isNotNull(loginIdDefaultNull)) {
                userId = Long.valueOf(loginIdDefaultNull.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //记录请求耗时
        Long costTime = System.currentTimeMillis() - TIME_HOLDER.get();
        //异步记录日志
        LogManager.me().executeExceptionLog(opLog, userId, joinPoint, exception, costTime);
    }

    /**
     * 记录请求耗时
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("getLogPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 将耗时传递给后续通知
        TIME_HOLDER.set(System.currentTimeMillis());
        try {
            return joinPoint.proceed();
        } finally {
            TIME_HOLDER.remove();
        }
    }
}
