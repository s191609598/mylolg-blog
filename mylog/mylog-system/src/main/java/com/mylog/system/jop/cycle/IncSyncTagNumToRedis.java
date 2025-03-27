package com.mylog.system.jop.cycle;

import com.mylog.system.service.SysTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 同步标签数量到redis
 *
 * @author pss
 * @date 2025/3/24
 */
//TODO 取消注解开启定时任务
@Component
@Slf4j
public class IncSyncTagNumToRedis {


    @Resource
    private SysTagService sysTagService;


    /**
     * 每小时执行一次
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void run() {
        sysTagService.queryHomeTagAll();
    }

}
