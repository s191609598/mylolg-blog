package com.mylog.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mylog.system.entity.article.SysArticleTag;
import com.mylog.system.entity.article.vo.TagNumVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文章标签关联表sys_article_tag表持久层接口
 *
 * @author pss
 * @since 2025-02-21 15:16:33
 */
@Mapper
public interface SysArticleTagDao extends BaseMapper<SysArticleTag> {

    List<TagNumVO> getTagNum();

}
