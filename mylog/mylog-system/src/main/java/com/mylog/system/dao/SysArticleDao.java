package com.mylog.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mylog.system.entity.article.SysArticle;
import com.mylog.system.entity.article.dto.ArticleEsDTO;
import com.mylog.system.entity.article.dto.SearchArticleByKeywordDTO;
import com.mylog.system.entity.article.vo.SearchArticleByKeywordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 文章表sys_article表持久层接口
 *
 * @author pss
 * @since 2025-02-21 15:16:33
 */
@Mapper
public interface SysArticleDao extends BaseMapper<SysArticle> {

    IPage<SearchArticleByKeywordVO> searchArticleByKeyword(@Param("dto") SearchArticleByKeywordDTO dto, @Param("iPage") IPage iPage);
    List<ArticleEsDTO> queryArticleEsAll();

    List<ArticleEsDTO> listArticleWithDelete(Date minUpdateTime);

}
