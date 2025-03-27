package com.mylog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.article.SysArticleTag;
import com.mylog.system.entity.tag.SysTag;

import java.util.List;

/**
 * @author pss
 * @description 针对表【sys_article_tag(文章标签关联表)】的数据库操作Service
 * @createDate 2025-02-21 15:34:21
 */
public interface SysArticleTagService extends IService<SysArticleTag> {

    boolean saveBatch(List<SysTag> tagList, Long articleId);

    List<SysArticleTag> queryByArticleId(Long articleId);

    boolean removeByArticleId(Long articleId);
    List<SysArticleTag> queryByTagId(Long tagId);

}
