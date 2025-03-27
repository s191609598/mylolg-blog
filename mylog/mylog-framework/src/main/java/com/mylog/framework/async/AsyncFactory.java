package com.mylog.framework.async;

import com.mylog.common.utils.SpringUtils;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.system.entity.SysLogError;
import com.mylog.system.service.LogErrorService;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author Pss
 * @date 2022/1/11 10:11
 */
@Slf4j
public class AsyncFactory {


    /**
     * 异常日志记录
     *
     * @param errorLog 异常日志信息
     * @return 任务task
     */
    public static TimerTask recordErrorLog(final SysLogError errorLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                errorLog.setOperLocation(IpUtils.getIpAddr(errorLog.getIp()));
                SpringUtils.getBean(LogErrorService.class).insertErrorlog(errorLog);
            }
        };
    }

    //todo  请求日志，登录日志


}
