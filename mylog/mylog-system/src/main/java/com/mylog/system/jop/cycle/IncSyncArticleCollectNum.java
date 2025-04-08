package com.mylog.system.jop.cycle;

import com.mylog.common.constant.RedisConstants;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.system.entity.article.SysArticle;
import com.mylog.system.service.SysArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 增量同步帖子收藏量
 * @author pss
 * @date 2025/4/2
 */
@Component
@Slf4j
public class IncSyncArticleCollectNum {
    @Resource
    SysArticleService sysArticleService;

    @Resource
    RedisCacheUtils redisCacheUtils;

//    @Scheduled(cron = "0 20 0 * * ? ")
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void run() {
        Collection<String> keys = redisCacheUtils.keys(RedisConstants.REDIS_ARTICLE_COLLECT_NUM + "*");
        if (StringUtils.isNotEmpty(keys)) {
            final int pageSize = 500;
            List<SysArticle> updateList = new ArrayList<>();
            keys.forEach(i -> {
                String[] split = i.split(":");
                if (split.length == 2) {
                    String articleId = split[1];
                    SysArticle article = sysArticleService.getById(articleId);
                    if (StringUtils.isNotNull(article)) {
                        SysArticle update = new SysArticle();
                        update.setId(article.getId());
                        update.setCollectNum(article.getCollectNum() + 1);
                        updateList.add(update);
                    }
                }
            });
            int total = updateList.size();
            for (int i = 0; i < total; i += pageSize) {
                int end = Math.min(i + pageSize, total);
                log.info("sync collectNum {} to {}", i, end);
                sysArticleService.updateBatchById(updateList.subList(i, end));
            }
        }
    }
}
