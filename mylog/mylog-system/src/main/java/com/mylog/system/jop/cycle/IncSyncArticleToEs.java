package com.mylog.system.jop.cycle;

import com.mylog.common.utils.StringUtils;
import com.mylog.system.dao.ArticleEsDao;
import com.mylog.system.entity.article.dto.ArticleEsDTO;
import com.mylog.system.service.SysArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 增量同步帖子到 es
 *
 * @author pss
 * @date 2025/3/24
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class IncSyncArticleToEs {

    @Resource
    private SysArticleService sysArticleService;

    @Resource
    private ArticleEsDao articleEsDao;

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
        List<ArticleEsDTO> list = sysArticleService.listArticleWithDelete(fiveMinutesAgoDate);
        if (StringUtils.isEmpty(list)) {
            log.info("no inc Article");
            return;
        }
        final int pageSize = 500;
        int total = list.size();
        log.info("IncSyncArticleToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            articleEsDao.saveAll(list.subList(i, end));
        }
        log.info("IncSyncArticleToEs end, total {}", total);
    }
}
