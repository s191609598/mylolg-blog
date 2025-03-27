package com.mylog.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.article.SysArticle;
import com.mylog.system.entity.article.dto.*;
import com.mylog.system.entity.article.vo.*;

import java.util.Date;
import java.util.List;

/**
 * @author pss
 * @description 针对表【sys_article(文章表)】的数据库操作Service
 * @createDate 2025-02-21 15:28:06
 */
public interface SysArticleService extends IService<SysArticle> {

    /**
     * 新增文章
     *
     * @param dto
     * @return
     */
    boolean addArticle(EditArticleDTO dto);

    /**
     * 修改文章
     *
     * @param dto
     * @return
     */
    boolean updateArticle(EditArticleDTO dto);

    /**
     * 修改文章状态
     *
     * @param
     * @return
     */
    boolean updateArticleStatus(UpdateArticleStatusDTO dto);

    /**
     * 获取文章详情
     *
     * @param id
     * @return
     */
    ArticleVO getArticleById(Long id);

    /**
     * 获取文章列表(后台)
     *
     * @param dto
     * @return
     */
    Page<QueryArticleVO> queryArticleList(QueryArticleDTO dto);

    /**
     * 删除文章
     */
    boolean deleteArticle(Long id);

    /**
     * 首页-文章列表
     * @return
     */
    IPage<HomeArticleListVO> queryHomeArticleList(HomeArticleDTO dto);

    /**
     * 首页-文章详情
     * @param id
     * @return
     */
    HomeArticleVO getHomeArticleById(Long id);

    /**
     * 首页-获取轮播图
     * @return
     */
    List<ArticleCarouselVO> queryArticleCarouselAll();

    List<RecommendArticleVO> queryRecommendArticle();

    /**
     * 首页-搜索文章
     * @param dto
     * @return
     */
    IPage<SearchArticleByKeywordVO> searchArticleByKeyword(SearchArticleByKeywordDTO dto);

    /**
     * 从ES查询
     * @param dto
     * @return
     */
    IPage<SearchArticleByKeywordVO> searchFromEs(SearchArticleByKeywordDTO dto);

    /**
     * 获取所有ES需要的数据
     * @return
     */
    List<ArticleEsDTO> queryArticleEsAll();

    /**
     * 根据分类查询文章
     * @param categoryId
     * @return
     */
    List<SysArticle> queryByCategoryId(Long categoryId);

    List<ArticleEsDTO> listArticleWithDelete(Date minUpdateTime);

}
