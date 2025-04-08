package com.mylog.system.jop.once;

/**
 * @author pss
 * @date 2025/3/12 22:07
 */

import com.mylog.common.utils.StringUtils;
import com.mylog.system.dao.ArticleEsDao;
import com.mylog.system.entity.article.dto.ArticleEsDTO;
import com.mylog.system.service.SysArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 全量同步数据到ES
 * @Author: pss
 * @CreateTime: 2025-03-12
 * @Version: 1.0
 */
//TODO 取消注解开启定时任务
@Component
@Slf4j
public class FullSyncArticleToEs implements CommandLineRunner {

    @Resource
    private SysArticleService sysArticleService;

    @Resource
    private ArticleEsDao articleEsDao;

    @Override
    public void run(String... args) {
        // 全量获取题目（数据量不大（千万以内）的情况下使用） 数据量大就用异步编排
        List<ArticleEsDTO> list = sysArticleService.queryArticleEsAll();
        if (StringUtils.isEmpty(list)) {
            return;
        }
        // 分页批量插入到 ES
        final int pageSize = 500;
        int total = list.size();
        log.info("FullSyncArticleToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            // 注意同步的数据下标不能超过总数据量
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            articleEsDao.saveAll(list.subList(i, end));
        }
        log.info("FullSyncArticleToEs end, total {}", total);
    }
}
