package com.mylog.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysArticleTagDao;
import com.mylog.system.entity.article.SysArticleTag;
import com.mylog.system.entity.tag.SysTag;
import com.mylog.system.service.SysArticleTagService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pss
 * @description 针对表【sys_article_tag(文章标签关联表)】的数据库操作Service实现
 * @createDate 2025-02-21 15:34:21
 */
@Service
public class SysArticleTagServiceImpl extends ServiceImpl<SysArticleTagDao, SysArticleTag> implements SysArticleTagService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(List<SysTag> tagList, Long articleId) {
        SysArticleTagService sysArticleTagService = (SysArticleTagService) AopContext.currentProxy();
        sysArticleTagService.removeByArticleId(articleId);
        if (StringUtils.isEmpty(tagList) || StringUtils.isNull(articleId)) {
            return true;
        }
        List<Long> ids = tagList.stream().map(SysTag::getId).collect(Collectors.toList());
        final int pageSize = 500;
        int total = ids.size();
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            List<Long> longs = ids.subList(i, end);
            List<SysArticleTag> list = new ArrayList<>();
            for (Long tagId : longs) {
                SysArticleTag sysArticleTag = new SysArticleTag();
                sysArticleTag.setTagId(tagId);
                sysArticleTag.setArticleId(articleId);
                list.add(sysArticleTag);
            }
            boolean b = this.saveBatch(list);
            AssertUtils.assertIf(!b, ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public List<SysArticleTag> queryByArticleId(Long articleId) {
        if (StringUtils.isNull(articleId)) {
            return null;
        }
        QueryWrapper<SysArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("articleId", articleId);
        List<SysArticleTag> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public boolean removeByArticleId(Long articleId) {
        QueryWrapper<SysArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("articleId", articleId);
        return this.remove(queryWrapper);
    }

    @Override
    public List<SysArticleTag> queryByTagId(Long tagId) {
        if (StringUtils.isNull(tagId)) {
            return null;
        }
        QueryWrapper<SysArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tagId", tagId);
        List<SysArticleTag> list = this.list(queryWrapper);
        return list;
    }
}




