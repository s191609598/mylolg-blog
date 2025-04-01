package com.mylog.framework.async;

import com.mylog.common.utils.SpringUtils;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.system.entity.SysErrorLog;
import com.mylog.system.service.SysErrorLogService;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author Pss
 */
@Slf4j
public class AsyncFactory {


    /**
     * 异常日志记录
     *
     * @param errorLog 异常日志信息
     * @return 任务task
     */
    public static TimerTask recordErrorLog(final SysErrorLog errorLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                errorLog.setOperLocation(IpUtils.getIpAddr(errorLog.getIp()));
                SpringUtils.getBean(SysErrorLogService.class).insertErrorlog(errorLog);
            }
        };
    }

}
