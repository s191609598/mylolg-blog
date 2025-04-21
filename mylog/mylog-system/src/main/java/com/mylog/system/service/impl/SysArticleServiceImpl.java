package com.mylog.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.Constants;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.utils.ConvertUtils;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysArticleDao;
import com.mylog.system.entity.article.SysArticle;
import com.mylog.system.entity.article.SysArticleTag;
import com.mylog.system.entity.article.dto.*;
import com.mylog.system.entity.article.vo.*;
import com.mylog.system.entity.category.SysCategory;
import com.mylog.system.entity.comment.SysComment;
import com.mylog.system.entity.home.dto.QueryMyCollectDTO;
import com.mylog.system.entity.home.vo.QueryMyCollectVO;
import com.mylog.system.entity.tag.SysTag;
import com.mylog.system.entity.user.SysUser;
import com.mylog.system.service.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author pss
 * @description 针对表【sys_article(文章表)】的数据库操作Service实现
 * @createDate 2025-02-21 15:28:06
 */
@Service
public class SysArticleServiceImpl extends ServiceImpl<SysArticleDao, SysArticle> implements SysArticleService {

    @Resource
    SysTagService sysTagService;

    @Resource
    SysArticleTagService sysArticleTagService;
    @Resource
    SysUserService sysUserService;

    @Resource
    SysCategoryService sysCategoryService;
    @Resource
    SysCommentService sysCommentService;
    @Resource
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    RedisCacheUtils redisCacheUtils;

    @Resource
    SysArticleUpService sysArticleUpService;
    @Resource
    SysArticleCollectService sysArticleCollectService;


    /**
     * 新增文章
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addArticle(EditArticleDTO dto) {
        //将EditArticleDTO转换为SysArticle
        SysArticle sysArticle = ConvertUtils.sourceToTarget(dto, SysArticle.class);
        sysArticle.setId(null);
        //获取当前登录用户的ID
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        //如果当前用户未登录，则抛出异常
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        //设置文章的创建者
        sysArticle.setCreateBy(Long.valueOf(loginIdDefaultNull.toString()));
        //默认状态为私密 需要手动发布
        sysArticle.setState(Constants.ARTICL_STATUS_2);
        //保存文章
        boolean save = this.save(sysArticle);
        //如果保存失败，则抛出异常
        AssertUtils.assertIf(!save, ErrorCode.OPERATION_ERROR);
        //获取文章的标签
        List<String> tags = dto.getTags();
        //如果标签不为空
        if (CollUtil.isNotEmpty(tags)) {
            //根据标签名称查询标签
            List<SysTag> tagList = sysTagService.selectByNames(tags);
            //如果标签不为空
            if (CollUtil.isNotEmpty(tagList)) {
                //将标签与文章关联
                boolean b = sysArticleTagService.saveBatch(tagList, sysArticle.getId());
                if (b) {
                    Set<String> keys = new HashSet<>();
                    keys.add(RedisConstants.REDIS_ARTICLE_CAROUSEL);
                    keys.add(RedisConstants.REDIS_ARTICLE_RECOMMEND);
                    keys.add(RedisConstants.REDIS_TAG);
                    keys.add(RedisConstants.REDIS_ARTICLE + dto.getId());
                    keys.add(RedisConstants.REDIS_ARTICLE_ADMIN + dto.getId());
                    redisCacheUtils.deleteObject(keys);
                }
            }
        }
        //返回保存结果
        return save;
    }

    /**
     * 修改文章
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateArticle(EditArticleDTO dto) {
        SysArticle sysArticle = ConvertUtils.sourceToTarget(dto, SysArticle.class);
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        SysArticle byId = this.getById(sysArticle.getId());
        AssertUtils.isNull(byId, ErrorCode.NOT_FOUND_ERROR);
        sysArticle.setUpdateBy(Long.valueOf(loginIdDefaultNull.toString()));
        sysArticle.setUpdateTime(new Date());
        //修改后默认状态为私密 需要手动发布
        sysArticle.setState(Constants.ARTICL_STATUS_2);
        boolean update = this.updateById(sysArticle);
        AssertUtils.assertIf(!update, ErrorCode.OPERATION_ERROR);
        List<String> tags = dto.getTags();
        if (CollUtil.isNotEmpty(tags)) {
            List<SysTag> tagList = sysTagService.selectByNames(tags);
            if (CollUtil.isNotEmpty(tagList)) {
                boolean b = sysArticleTagService.saveBatch(tagList, sysArticle.getId());
                if (b) {
                    redisCacheUtils.deleteObject(RedisConstants.REDIS_TAG);
                }
            }
        }
        Set<String> keys = new HashSet<>();
        keys.add(RedisConstants.REDIS_ARTICLE_CAROUSEL);
        keys.add(RedisConstants.REDIS_ARTICLE_RECOMMEND);
        keys.add(RedisConstants.REDIS_TAG);
        keys.add(RedisConstants.REDIS_ARTICLE + dto.getId());
        keys.add(RedisConstants.REDIS_ARTICLE_ADMIN + dto.getId());
        redisCacheUtils.deleteObject(keys);
        return update;
    }

    /**
     * 修改文章状态
     *
     * @param dto 修改文章状态DTO
     * @return 是否修改成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateArticleStatus(UpdateArticleStatusDTO dto) {
        // 根据文章ID获取文章信息
        SysArticle byId = this.getById(dto.getId());
        // 判断文章是否存在
        AssertUtils.isNull(byId, ErrorCode.NOT_FOUND_ERROR);
        // 获取当前登录用户ID
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        // 判断用户是否登录
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        // 创建新的文章对象
        SysArticle sysArticle = new SysArticle();
        // 设置文章ID
        sysArticle.setId(dto.getId());
        // 设置文章状态
        sysArticle.setState(dto.getStatus());
        // 设置修改时间
        sysArticle.setEditTime(new Date());
        sysArticle.setUpdateTime(new Date());
        // 更新文章信息
        boolean b = this.updateById(sysArticle);
        if (b) {
            Set<String> keys = new HashSet<>();
            keys.add(RedisConstants.REDIS_ARTICLE_CAROUSEL);
            keys.add(RedisConstants.REDIS_ARTICLE_RECOMMEND);
            keys.add(RedisConstants.REDIS_TAG);
            keys.add(RedisConstants.REDIS_ARTICLE + dto.getId());
            keys.add(RedisConstants.REDIS_ARTICLE_ADMIN + dto.getId());
            redisCacheUtils.deleteObject(keys);
        }
        // 返回更新结果
        return b;
    }

    /**
     * 获取文章详情
     *
     * @param id
     * @return
     */
    @Override
    public ArticleVO getArticleById(Long id) {
        AssertUtils.isNull(id, ErrorCode.PARAMS_ERROR);
        ArticleVO vo = null;
        vo = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_ADMIN + id);
        if (StringUtils.isNull(vo)){
            SysArticle byId = this.getById(id);
            AssertUtils.isNull(byId, ErrorCode.NOT_FOUND_ERROR);
            vo = ConvertUtils.sourceToTarget(byId, ArticleVO.class);
            List<SysArticleTag> list = sysArticleTagService.queryByArticleId(id);
            if (CollUtil.isNotEmpty(list)) {
                List<Long> tagIds = list.stream().map(SysArticleTag::getTagId).collect(Collectors.toList());
                List<SysTag> tags = sysTagService.listByIds(tagIds);
                if (CollUtil.isNotEmpty(tags)) {
                    List<String> tagNames = tags.stream().map(SysTag::getName).collect(Collectors.toList());
                    vo.setTags(tagNames);
                }
            }
            if (StringUtils.isEmpty(vo.getTags())) {
                vo.setTags(new ArrayList<>());
            }
//        获取创建人名称
            if (StringUtils.isNotNull(vo.getCreateBy())) {
                SysUser sysUser = sysUserService.getUserById(vo.getCreateBy());
                vo.setCreateByName(sysUser.getUserName());
            }
            //获取文章类型
            if (StringUtils.isNotNull(vo.getCategoryId())) {
                SysCategory sysCategory = sysCategoryService.getById(vo.getCategoryId());
                if (StringUtils.isNotNull(sysCategory)) {
                    vo.setCategoryName(sysCategory.getName());
                }
            }
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_ADMIN + id, vo, 60 * 60 * 24, TimeUnit.SECONDS);
        }
        return vo;
    }

    /**
     * 获取文章列表(后台)
     *
     * @param dto
     * @return
     */
    @Override
    public Page<QueryArticleVO> queryArticleList(QueryArticleDTO dto) {
        QueryWrapper<SysArticle> queryWrapper = this.getQueryWrapper(dto);
        Page<SysArticle> iPage = baseMapper.selectPage(new Page<>(dto.getPageNo(), dto.getPageSize()), queryWrapper);
        Page<QueryArticleVO> page = new Page<>();
        page.setCurrent(iPage.getCurrent());
        page.setTotal(iPage.getTotal());
        page.setSize(iPage.getSize());
        page.setPages(iPage.getPages());
        page.setRecords(ConvertUtils.sourceToTarget(iPage.getRecords(), QueryArticleVO.class));
        if (StringUtils.isNotEmpty(page.getRecords())) {
            for (QueryArticleVO vo : page.getRecords()) {
                List<SysArticleTag> list = sysArticleTagService.queryByArticleId(vo.getId());
                if (CollUtil.isNotEmpty(list)) {
                    List<Long> tagIds = list.stream().map(SysArticleTag::getTagId).collect(Collectors.toList());
                    List<SysTag> tags = sysTagService.listByIds(tagIds);
                    if (CollUtil.isNotEmpty(tags)) {
                        List<String> tagNames = tags.stream().map(SysTag::getName).collect(Collectors.toList());
                        vo.setTags(tagNames);
                    }
                }
                if (StringUtils.isEmpty(vo.getTags())) {
                    vo.setTags(new ArrayList<>());
                }
//                //        获取创建人名称
//                if (StringUtils.isNotNull(vo.getCreateBy())) {
//                    SysUser sysUser = sysUserService.getUserById(vo.getCreateBy());
//                    vo.setCreateByName(sysUser.getUserName());
//                }
                //获取文章类型
                if (StringUtils.isNotNull(vo.getCategoryId())) {
                    SysCategory sysCategory = sysCategoryService.getById(vo.getCategoryId());
                    if (StringUtils.isNotNull(sysCategory)) {
                        vo.setCategoryName(sysCategory.getName());
                    }
                }
            }
        }
        return page;
    }

    /**
     * 删除文章
     */
    @Override
    public boolean deleteArticle(Long id) {
        return this.removeById(id);
    }

    @Override
    public IPage<HomeArticleListVO> queryHomeArticleList(HomeArticleDTO dto) {
        QueryWrapper<SysArticle> wq = new QueryWrapper<>();
        wq.ne("state", 2);
        wq.eq("isCarousel", 0);
        wq.eq("isRecommend", 0);
        if (dto.getShowTop()) {
            wq.orderByDesc("isTop");
        }
        wq.orderByDesc("createTime");
        Page<SysArticle> iPage = baseMapper.selectPage(new Page<>(dto.getPageNo(), dto.getPageSize()), wq);
        Page<HomeArticleListVO> page = new Page<>();
        page.setCurrent(iPage.getCurrent());
        page.setTotal(iPage.getTotal());
        page.setSize(iPage.getSize());
        page.setPages(iPage.getPages());
        page.setRecords(ConvertUtils.sourceToTarget(iPage.getRecords(), HomeArticleListVO.class));
        if (StringUtils.isNotEmpty(page.getRecords())) {
            for (HomeArticleListVO vo : page.getRecords()) {
                //获取文章类型
                if (StringUtils.isNotNull(vo.getCategoryId())) {
                    SysCategory sysCategory = sysCategoryService.getById(vo.getCategoryId());
                    if (StringUtils.isNotNull(sysCategory)) {
                        vo.setCategoryName(sysCategory.getName());
                    }
                }
                //获取评论数量
                vo.setCommentNum(sysCommentService.queryByArticleIdCount(vo.getId()));
            }
        }
        return page;
    }

    @Override
    public HomeArticleVO getHomeArticleById(Long id) {
        HomeArticleVO vo = null;
        vo = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE + id);
        if (StringUtils.isNull(vo)) {
            ArticleVO byId = this.getArticleById(id);
            AssertUtils.isNull(byId, ErrorCode.NOT_FOUND_ERROR);
            vo = ConvertUtils.sourceToTarget(byId, HomeArticleVO.class);
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE + id, vo, 60 * 60 * 24, TimeUnit.SECONDS);
        }
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        Long userId = null;
        if (StringUtils.isNotNull(loginIdDefaultNull)) {
            userId = Long.valueOf(loginIdDefaultNull.toString());
            //获得是否点赞
            vo.setIsUp(sysArticleUpService.getIsUp(id, userId));
            //获得是否收藏
            vo.setIsCollect(sysArticleCollectService.getIsCollect(id, userId));
        } else {
            //判断文章是否需要登录查看
            if (vo.getState().toString().equals(Constants.ARTICL_STATUS_1.toString())) {
                vo.setContent(null);
            }
        }
        //获取评论数量
        Integer commentNum = this.getCommentNum(id);
        vo.setCommentNum(commentNum);
        //阅读数量
        this.getReadNum(id, true);
        //获得点赞数量
        Integer upNum = sysArticleUpService.getUpNum(id);
        vo.setUpNum(upNum);
        //获得收藏数量
        Integer collectNum = sysArticleCollectService.getCollectNum(id);
        vo.setCollectNum(collectNum);
        return vo;
    }

    @Override
    public List<ArticleCarouselVO> queryArticleCarouselAll() {
        List<ArticleCarouselVO> voList = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_CAROUSEL);
        if (StringUtils.isEmpty(voList)) {
            QueryWrapper<SysArticle> wq = new QueryWrapper<>();
            wq.eq("isCarousel", 1);
            wq.in("state", Constants.ARTICL_STATUS_0, Constants.ARTICL_STATUS_1);
            wq.orderByAsc("sort");
            List<SysArticle> list = this.list(wq);
            if (StringUtils.isNotEmpty(list)) {
                voList = ConvertUtils.sourceToTarget(list, ArticleCarouselVO.class);
                redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_CAROUSEL, voList, 60 * 60 * 24, TimeUnit.SECONDS);
            }
        }
        return voList;
    }

    @Override
    public List<RecommendArticleVO> queryRecommendArticle() {
        List<RecommendArticleVO> voList = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_RECOMMEND);
        if (StringUtils.isEmpty(voList)) {
            QueryWrapper<SysArticle> wq = new QueryWrapper<>();
            wq.eq("isRecommend", 1);
            wq.in("state", Constants.ARTICL_STATUS_0, Constants.ARTICL_STATUS_1);
            wq.orderByAsc("sort");
            List<SysArticle> list = this.list(wq);
            if (StringUtils.isNotEmpty(list)) {
                voList = ConvertUtils.sourceToTarget(list, RecommendArticleVO.class);
                redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_RECOMMEND, voList, 60 * 60 * 24, TimeUnit.SECONDS);
            }
        }
        return voList;
    }

    @Override
    public IPage<SearchArticleByKeywordVO> searchArticleByKeyword(SearchArticleByKeywordDTO dto) {
        if (StringUtils.isNotBlank(dto.getKeyword()) || StringUtils.isNotNull(dto.getTagId()) || StringUtils.isNotNull(dto.getCategoryId())) {
            IPage<SearchArticleByKeywordVO> searchArticleByKeywordVOIPage = baseMapper.searchArticleByKeyword(dto, new Page(dto.getPageNo(), dto.getPageSize()));
            searchArticleByKeywordVOIPage.getRecords().forEach(i -> {
                if (StringUtils.isNotNull(i.getCategoryId())) {
                    SysCategory byId = sysCategoryService.getById(i.getCategoryId());
                    if (StringUtils.isNotNull(byId)) {
                        i.setCategoryName(byId.getName());
                    }
                }
            });
            return searchArticleByKeywordVOIPage;
        }
        return null;
    }

    @Override
    public IPage<SearchArticleByKeywordVO> searchFromEs(SearchArticleByKeywordDTO dto) {
        String keyword = dto.getKeyword();
        int pageNo = dto.getPageNo() - 1;
        int pageSize = dto.getPageSize();
        // 构造查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        boolQueryBuilder.filter(QueryBuilders.termsQuery("state", Arrays.asList(0, 1)));
        if (StringUtils.isNotNull(dto.getTagId())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("tagsids", dto.getTagId()));
        }
        if (StringUtils.isNotNull(dto.getCategoryId())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryId", dto.getCategoryId()));
        }
        // 按关键词检索
        if (StringUtils.isNotBlank(keyword)) {
            // title = '' or content = '' or answer = ''
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", keyword));
            boolQueryBuilder.should(QueryBuilders.matchQuery("excerpt", keyword));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", keyword));
            boolQueryBuilder.should(QueryBuilders.matchQuery("tags", keyword));
            boolQueryBuilder.should(QueryBuilders.matchQuery("categoryName", keyword));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 排序
        // 排序条件
        FieldSortBuilder isTopSortBuilder = null;
        if (dto.getShowTop()) {
            isTopSortBuilder = SortBuilders.fieldSort("isTop").order(SortOrder.DESC);
        }
        SortBuilder<?> sortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        // 分页
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = null;
        if (dto.getShowTop()) {
            searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(pageRequest).withSorts(isTopSortBuilder).withSorts(sortBuilder).build();
        } else {
            searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(pageRequest).withSorts(sortBuilder).build();
        }
        SearchHits<ArticleEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, ArticleEsDTO.class);
        // 复用 MyBatis Plus 的分页对象，封装返回结果
        Page<SearchArticleByKeywordVO> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<SearchArticleByKeywordVO> resourceList = new ArrayList<>();
        if (searchHits.hasSearchHits()) {
            List<SearchHit<ArticleEsDTO>> searchHitList = searchHits.getSearchHits();
            for (SearchHit<ArticleEsDTO> questionEsDTOSearchHit : searchHitList) {
                SearchArticleByKeywordVO searchArticleByKeywordVO = ConvertUtils.sourceToTarget(questionEsDTOSearchHit.getContent(), SearchArticleByKeywordVO.class);
                searchArticleByKeywordVO.setContent(null);
                resourceList.add(searchArticleByKeywordVO);
                Long id = searchArticleByKeywordVO.getId();
                //阅读数量
                Integer readNum = this.getReadNum(id, false);
                searchArticleByKeywordVO.setReadNum(readNum);
                //获得点赞数量
                Integer upNum = sysArticleUpService.getUpNum(id);
                searchArticleByKeywordVO.setUpNum(upNum);
                //获得收藏数量
                Integer collectNum = sysArticleCollectService.getCollectNum(id);
                searchArticleByKeywordVO.setCollectNum(collectNum);
                //获得评论数量
                Integer commentNum = this.getCommentNum(id);
                searchArticleByKeywordVO.setCommentNum(commentNum);
            }
        }
        page.setRecords(resourceList);
        return page;
    }

    @Override
    public List<ArticleEsDTO> queryArticleEsAll() {
        List<ArticleEsDTO> articleEsDTOS = baseMapper.queryArticleEsAll();
        if (StringUtils.isNotEmpty(articleEsDTOS)) {
            articleEsDTOS.forEach(i -> {
                i.setArticleId(i.getId());
                List<SysArticleTag> articleTagList = sysArticleTagService.queryByArticleId(i.getId());
                if (CollUtil.isNotEmpty(articleTagList)) {
                    List<Long> tagIds = articleTagList.stream().map(SysArticleTag::getTagId).collect(Collectors.toList());
                    List<SysTag> tags = sysTagService.listByIds(tagIds);
                    if (CollUtil.isNotEmpty(tags)) {
                        List<String> tagNames = tags.stream().map(SysTag::getName).collect(Collectors.toList());
                        i.setTags(tagNames);
                        i.setTagsids(tagIds);
                    }
                }
            });
        }
        return articleEsDTOS;
    }

    @Override
    public List<SysArticle> queryByCategoryId(Long categoryId) {
        QueryWrapper<SysArticle> wq = new QueryWrapper();
        wq.eq("categoryId", categoryId);
        List<SysArticle> sysArticles = baseMapper.selectList(wq);
        return sysArticles;
    }

    /**
     * 增量同步文章到ES
     *
     * @param minUpdateTime
     * @return
     */
    @Override
    public List<ArticleEsDTO> listSyncArticle(Date minUpdateTime) {
        List<ArticleEsDTO> articleEsDTOS = baseMapper.listSyncArticle(minUpdateTime);
        if (StringUtils.isNotEmpty(articleEsDTOS)) {
            articleEsDTOS.forEach(i -> {
                i.setArticleId(i.getId());
                List<SysArticleTag> articleTagList = sysArticleTagService.queryByArticleId(i.getId());
                if (CollUtil.isNotEmpty(articleTagList)) {
                    List<Long> tagIds = articleTagList.stream().map(SysArticleTag::getTagId).collect(Collectors.toList());
                    List<SysTag> tags = sysTagService.listByIds(tagIds);
                    if (CollUtil.isNotEmpty(tags)) {
                        List<String> tagNames = tags.stream().map(SysTag::getName).collect(Collectors.toList());
                        i.setTags(tagNames);
                        i.setTagsids(tagIds);
                    }
                }
            });
        }
        return articleEsDTOS;
    }

    @Override
    public IPage<QueryMyCollectVO> queryMyCollect(QueryMyCollectDTO dto) {
        IPage<QueryMyCollectVO> queryMyCollectVOIPage = null;
        String redisKey = RedisConstants.USER_COLLECT_KEY + dto.getUserId();
        boolean exists = redisCacheUtils.exists(redisKey);
        if (exists) {
            int pageNo = dto.getPageNo();
            int pageSize = dto.getPageSize();
            long start = (pageNo - 1) * pageSize;
            long end = start + pageSize - 1;
            Long total = redisCacheUtils.zsetSize(redisKey);
            long pages = Math.floorDiv(total, pageSize);
            Set<QueryMyCollectVO> zSet = redisCacheUtils.getZSetByRange(redisKey, start, end);
//            Boolean isNull = false;
//            for (QueryMyCollectVO queryMyCollectVO : zSet) {
//                if (StringUtils.isNull(queryMyCollectVO.getId())) {
//                    isNull = true;
//                    break;
//                }
//            }
//
//            if (total == 1 && isNull) {
//                queryMyCollectVOIPage.setRecords(Collections.emptyList());
//            } else {
//
//            }
            queryMyCollectVOIPage = new Page<>();
            queryMyCollectVOIPage.setRecords(new ArrayList<>(StringUtils.isNotEmpty(zSet) ? zSet : Collections.emptyList()));
            queryMyCollectVOIPage.setCurrent(pageNo);
            queryMyCollectVOIPage.setSize(pageSize);
            queryMyCollectVOIPage.setPages(pages);
            queryMyCollectVOIPage.setTotal(total);
        } else {
            List<QueryMyCollectVO> collectVOList = baseMapper.queryMyCollectAll(dto.getUserId());
            if (StringUtils.isNotEmpty(collectVOList)) {
                AtomicInteger atomicInteger = new AtomicInteger(collectVOList.size());
                collectVOList.forEach(i -> redisCacheUtils.zsetAdd(redisKey, i, atomicInteger.decrementAndGet()));
                redisCacheUtils.expire(redisKey, 60, TimeUnit.MINUTES);
            }
            IPage<QueryMyCollectVO> page = new Page<>(dto.getPageNo(), dto.getPageSize());
            queryMyCollectVOIPage = baseMapper.queryMyCollect(dto.getUserId(), page);
        }
        return queryMyCollectVOIPage;
    }

    @Override
    public Integer getReadNum(Long id, Boolean isRecord) {
        //阅读数量
        Integer readNum = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_READ_NUM + id);
        if (StringUtils.isNull(readNum)) {
            SysArticle sysArticle = this.getById(id);
            AssertUtils.isNull(sysArticle, ErrorCode.PARAMS_ERROR);
            if (StringUtils.isNull(sysArticle.getReadNum())) {
                readNum = 0;
            } else {
                readNum = sysArticle.getReadNum();
            }
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_READ_NUM + id, readNum, 60 * 60 * 24, TimeUnit.SECONDS);
        }
        //阅读量+1，后续在优化逻辑，暂定为登录的用户阅读就+1
        if (isRecord) {
            Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
            if (StringUtils.isNotNull(loginIdDefaultNull)) {
                redisCacheUtils.incrementValue(RedisConstants.REDIS_ARTICLE_READ_NUM + id);
            }
        }
        return readNum;
    }

    @Override
    public Integer getCommentNum(Long id) {
        Integer commentNum = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_COMMENT_TOTAL + id);
        if (StringUtils.isNull(commentNum)) {
            Integer i = sysCommentService.queryByArticleIdCount(id);
            if (StringUtils.isNull(i)) {
                commentNum = 0;
            } else {
                commentNum = i;
            }
            //缓存评论总数
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COMMENT_TOTAL + id, commentNum, 60 * 60 * 24, TimeUnit.SECONDS);
            //缓存评论根评论总数
            QueryWrapper<SysComment> wq = new QueryWrapper<>();
            wq.eq("articleId", id);
            wq.eq("isStick", 0);
            wq.isNull("pid");
            wq.isNull("rootId");
            wq.orderByAsc("createTime");
            Long count = sysCommentService.count(wq);
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COMMENT_ROOT_SIZE + id, Integer.valueOf(count.toString()), 60 * 60 * 24, TimeUnit.SECONDS);
        }
        return commentNum;
    }

    /**
     * 条件构造
     *
     * @param dto
     * @return
     */

    public QueryWrapper<SysArticle> getQueryWrapper(QueryArticleDTO dto) {
        AssertUtils.isNull(dto, ErrorCode.PARAMS_ERROR);
        QueryWrapper<SysArticle> queryWrapper = new QueryWrapper<>();
        Long id = dto.getId();
        String title = dto.getTitle();
        Long categoryId = dto.getCategoryId();
        String content = dto.getContent();
        String excerpt = dto.getExcerpt();
        Integer state = dto.getState();
        Integer articleType = dto.getArticleType();
        Integer isTop = dto.getIsTop();
        Integer isRecommend = dto.getIsRecommend();
        Integer isCarousel = dto.getIsCarousel();
        Integer minUpNum = dto.getMinUpNum();
        Integer maxUpNum = dto.getMaxUpNum();
        Integer minReadNum = dto.getMinReadNum();
        Integer maxReadNum = dto.getMaxReadNum();
        Integer minCommentNum = dto.getMinCommentNum();
        Integer maxCommentNum = dto.getMaxCommentNum();
        String sortField = dto.getSortField();
        String sortOrder = dto.getSortOrder();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);
        queryWrapper.eq(StringUtils.isNotNull(categoryId), "categoryId", categoryId);
        queryWrapper.eq(StringUtils.isNotNull(state), "state", state);
        queryWrapper.eq(StringUtils.isNotNull(articleType), "articleType", articleType);
        queryWrapper.eq(StringUtils.isNotNull(isTop), "isTop", isTop);
        queryWrapper.eq(StringUtils.isNotNull(isRecommend), "isRecommend", isRecommend);
        queryWrapper.eq(StringUtils.isNotNull(isCarousel), "isCarousel", isCarousel);
        queryWrapper.ge(StringUtils.isNotNull(minUpNum), "readNum", minUpNum);
        queryWrapper.le(StringUtils.isNotNull(maxUpNum), "readNum", maxUpNum);
        queryWrapper.ge(StringUtils.isNotNull(minReadNum), "upNum", minReadNum);
        queryWrapper.le(StringUtils.isNotNull(maxReadNum), "upNum", maxReadNum);
        queryWrapper.ge(StringUtils.isNotNull(minCommentNum), "commentNum", minCommentNum);
        queryWrapper.le(StringUtils.isNotNull(maxCommentNum), "commentNum", maxCommentNum);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(excerpt), "excerpt", excerpt);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), StringUtils.equals(sortOrder, Constants.ASC), sortField);
        return queryWrapper;
    }
}




