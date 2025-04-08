package com.mylog.system.jop.cycle;

import com.mylog.common.constant.RedisConstants;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.system.entity.SysArticleUp;
import com.mylog.system.service.SysArticleUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author pss
 * @date 2025/4/2
 */
@Component
@Slf4j
public class IncSyncArticleUpRecord {

    @Resource
    SysArticleUpService sysArticleUpService;

    @Resource
    RedisCacheUtils redisCacheUtils;

    @Scheduled(cron = "0 15 0 * * ? ")
    public void run() {
        Collection<String> keys = redisCacheUtils.keys(RedisConstants.REDIS_ARTICLE_UP_RECORD + "*");
        if (StringUtils.isNotEmpty(keys)) {
            final int pageSize = 500;
            List<SysArticleUp> adds = new ArrayList<>();
            keys.forEach(i -> {
                String[] split = i.split(":");
                if (split.length == 3) {
                    String userId = split[1];
                    String articleId = split[2];
                    SysArticleUp sysArticleUp = new SysArticleUp();
                    sysArticleUp.setCreateBy(Long.valueOf(userId));
                    sysArticleUp.setArticleId(Long.valueOf(articleId));
                    adds.add(sysArticleUp);
                }
            });
            int total = adds.size();
            for (int i = 0; i < total; i += pageSize) {
                int end = Math.min(i + pageSize, total);
                sysArticleUpService.saveBatch(adds.subList(i, end));
            }
        }
    }
}
