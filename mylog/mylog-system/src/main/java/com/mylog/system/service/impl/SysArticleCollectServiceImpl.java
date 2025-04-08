package com.mylog.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.utils.SpringContextUtils;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysArticleCollectDao;
import com.mylog.system.dao.SysArticleDao;
import com.mylog.system.entity.SysArticleCollect;
import com.mylog.system.entity.article.SysArticle;
import com.mylog.system.entity.article.vo.ArticleVO;
import com.mylog.system.entity.home.vo.QueryMyCollectVO;
import com.mylog.system.service.SysArticleCollectService;
import com.mylog.system.service.SysArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 彭上尚
 * @description 针对表【sys_article_collect(文章收藏记录表)】的数据库操作Service实现
 * @createDate 2025-04-02 22:01:57
 * <p>
 */
@Service
public class SysArticleCollectServiceImpl extends ServiceImpl<SysArticleCollectDao, SysArticleCollect> implements SysArticleCollectService {
    @Resource
    RedisCacheUtils redisCacheUtils;
    @Resource
    SysArticleDao sysArticleDao;


    @Override
    public Boolean collectArticle(Long articleId) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        String redisKey = RedisConstants.USER_COLLECT_KEY + userId;
        Set<QueryMyCollectVO> zSet = redisCacheUtils.getZSetByRange(redisKey, 0, -1);
        Boolean isNull = false;
        for (QueryMyCollectVO queryMyCollectVO : zSet) {
            if (StringUtils.isNull(queryMyCollectVO.getId())) {
                isNull = true;
                break;
            }
        }
        Long setSize = (long) zSet.size();
        QueryMyCollectVO vo = new QueryMyCollectVO();
        SysArticleService sysArticleService = SpringContextUtils.getBean(SysArticleService.class);
        ArticleVO articleVO = sysArticleService.getArticleById(articleId);
        AssertUtils.isNull(articleVO, ErrorCode.PARAMS_ERROR);
        vo.setId(articleId);
        vo.setTitle(articleVO.getTitle());
        vo.setExcerpt(articleVO.getExcerpt());
        if (StringUtils.isNotNull(setSize) && setSize > 0 && !isNull) {
            redisCacheUtils.zsetAdd(redisKey, vo, setSize);
        } else {
            redisCacheUtils.zsetAdd(redisKey, vo, 0);
            //删除空数据
            QueryMyCollectVO delVO = new QueryMyCollectVO();
            redisCacheUtils.zsetRemove(redisKey, delVO);
        }
        Integer isCollectRecord = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_COLLECT_RECORD + userId + ":" + articleId);
        if (StringUtils.isNull(isCollectRecord) || isCollectRecord == 0) {
            redisCacheUtils.incrementValue(RedisConstants.REDIS_ARTICLE_COLLECT_NUM + articleId);
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COLLECT_RECORD + userId + ":" + articleId, 1, 25, TimeUnit.HOURS);
        }
        return true;
    }

    @Override
    public Boolean noArticle(Long articleId) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        String redisKey = RedisConstants.USER_COLLECT_KEY + userId;
        Set<QueryMyCollectVO> zSet = redisCacheUtils.getZSetByRange(redisKey, 0, -1);
        QueryMyCollectVO delVO = new QueryMyCollectVO();
        for (QueryMyCollectVO i : zSet) {
            if (i.getId().toString().equals(articleId.toString())) {
                delVO.setId(i.getId());
                delVO.setTitle(i.getTitle());
                delVO.setExcerpt(i.getExcerpt());
                delVO.setCreateTime(i.getCreateTime());
                break;
            }
        }
        redisCacheUtils.zsetRemove(redisKey, delVO);
//        Long size = redisCacheUtils.zsetSize(redisKey);
//        if (size < 1) {
//            QueryMyCollectVO vo = new QueryMyCollectVO();
//            redisCacheUtils.zsetAdd(redisKey, vo, 0);
//        }
        Integer isCollectRecord = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_COLLECT_RECORD + userId + ":" + articleId);
        if (StringUtils.isNotNull(isCollectRecord) && isCollectRecord == 1) {
            redisCacheUtils.decrement(RedisConstants.REDIS_ARTICLE_COLLECT_NUM + articleId);
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COLLECT_RECORD + userId + ":" + articleId, 0, 25, TimeUnit.HOURS);
        }
        QueryWrapper<SysArticleCollect> wq = new QueryWrapper();
        wq.eq("articleId", articleId);
        wq.eq("createBy", userId);
        this.remove(wq);
        return true;
    }

    @Override
    public Integer getCollectNum(Long articleId) {
        Integer num = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_COLLECT_NUM + articleId);
        if (StringUtils.isNull(num)) {
            SysArticle sysArticle = sysArticleDao.selectById(articleId);
            if (StringUtils.isNotNull(sysArticle)) {
                num = sysArticle.getCollectNum();
                if (StringUtils.isNull(num)) {
                    num = 0;
                }
            } else {
                num = 0;
            }
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COLLECT_NUM + articleId, num);
        }
        return num;
    }

    @Override
    public Boolean getIsCollect(Long articleId, Long userId) {
        Integer isCollectRecord = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_COLLECT_RECORD + userId + ":" + articleId);
        Boolean isCollect = false;
        if (StringUtils.isNull(isCollectRecord)) {
            QueryWrapper<SysArticleCollect> wq = new QueryWrapper<>();
            wq.eq("articleId", articleId);
            wq.eq("createBy", userId);
            long count = this.count(wq);
            if (count > 0) {
                isCollect = true;
            } else {
                isCollect = false;
            }
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COLLECT_RECORD + userId + ":" + articleId, isCollect ? 1 : 0, 25, TimeUnit.HOURS);
        } else if (isCollectRecord == 0) {
            isCollect = false;
        } else {
            isCollect = true;
        }
        return isCollect;
    }
}




