package com.mylog.framework.initialization;

import com.mylog.common.constant.Constants;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.system.entity.SysConfig;
import com.mylog.system.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目初始化值
 *
 * @author pss
 * @date 2025/4/9
 */
@Component
@Slf4j
public class CacheInit implements CommandLineRunner {

    @Resource
    RedisCacheUtils redisCacheUtils;

    @Resource
    SysConfigService sysConfigService;


    @Override
    public void run(String... args) throws Exception {
        try {
            //备案号
            SysConfig beianhao = sysConfigService.getConfigByKey(Constants.SYS_BEIANHAO);
            if (StringUtils.isNotNull(beianhao) && StringUtils.isNotBlank(beianhao.getConfigValue())) {
                redisCacheUtils.setCacheObjectForever(RedisConstants.SYS_BEIANHAO_KEY, beianhao.getConfigValue());
            }
        } catch (Exception e) {
            log.error("初始化失败:{}", e.getMessage());
        }

    }
}
