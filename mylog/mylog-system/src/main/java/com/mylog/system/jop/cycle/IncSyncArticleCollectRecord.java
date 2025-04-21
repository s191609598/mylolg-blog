package com.mylog.system.jop.cycle;

import com.mylog.common.constant.RedisConstants;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.system.entity.SysArticleCollect;
import com.mylog.system.service.SysArticleCollectService;
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
public class IncSyncArticleCollectRecord {

    @Resource
    SysArticleCollectService sysArticleCollectService;

    @Resource
    RedisCacheUtils redisCacheUtils;

//        @Scheduled(cron = "0 25 0 * * ? ")
        @Scheduled(cron = "0 0 0/1 * * ? ")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void run() {
        Collection<String> keys = redisCacheUtils.keys(RedisConstants.REDIS_ARTICLE_COLLECT_RECORD + "*");
        if (StringUtils.isNotEmpty(keys)) {
            final int pageSize = 500;
            List<SysArticleCollect> adds = new ArrayList<>();
            List<SysArticleCollect> deletes = new ArrayList<>();
            keys.forEach(i -> {
                String[] split = i.split(":");
                if (split.length == 3) {
                    String userId = split[1];
                    String articleId = split[2];
                    Integer isCollect = redisCacheUtils.getCacheObject(i);
                    SysArticleCollect sysArticleCollect = new SysArticleCollect();
                    sysArticleCollect.setCreateBy(Long.valueOf(userId));
                    sysArticleCollect.setArticleId(Long.valueOf(articleId));
                    if (isCollect > 0) {
                        adds.add(sysArticleCollect);
                    } else {
                        deletes.add(sysArticleCollect);
                    }
                }
            });
            //新增处理
            if (StringUtils.isNotEmpty(adds)) {
                int total = adds.size();
                for (int i = 0; i < total; i += pageSize) {
                    int end = Math.min(i + pageSize, total);
                    sysArticleCollectService.saveBatch(adds.subList(i, end));
                }
            }
            //删除处理
            if (StringUtils.isNotEmpty(deletes)) {
                int total = deletes.size();
                for (int i = 0; i < total; i += pageSize) {
                    int end = Math.min(i + pageSize, total);
                    sysArticleCollectService.deleteCollects(deletes.subList(i, end));
                }
            }
            redisCacheUtils.deleteObject(keys);
        }
    }
}
