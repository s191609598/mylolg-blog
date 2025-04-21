package com.mylog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.SysArticleCollect;

import java.util.List;

/**
* @author 彭上尚
* @description 针对表【sys_article_collect(文章收藏记录表)】的数据库操作Service
* @createDate 2025-04-02 22:01:57
*/
public interface SysArticleCollectService extends IService<SysArticleCollect> {

    Boolean collectArticle(Long articleId);
    Boolean noArticle(Long articleId);

    Integer getCollectNum(Long articleId);
    Boolean getIsCollect(Long articleId,Long userId);

    Boolean deleteCollects(List<SysArticleCollect> deletes );

}
