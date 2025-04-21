package com.mylog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.SysArticleCollect;
import com.mylog.system.entity.SysArticleUp;

import java.util.List;

/**
 * @author 彭上尚
 * @description 针对表【sys_article_up(文章点赞记录表)】的数据库操作Service
 * @createDate 2025-04-02 22:01:57
 */
public interface SysArticleUpService extends IService<SysArticleUp> {

    Boolean upArticle(Long articleId);

    Boolean noArticle(Long articleId);

    Integer getUpNum(Long articleId);
    Boolean getIsUp(Long articleId,Long userId);

    Boolean deleteCollects(List<SysArticleUp> deletes );
}
