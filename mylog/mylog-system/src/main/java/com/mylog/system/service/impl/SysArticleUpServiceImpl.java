package com.mylog.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysArticleDao;
import com.mylog.system.dao.SysArticleUpDao;
import com.mylog.system.entity.SysArticleUp;
import com.mylog.system.entity.article.SysArticle;
import com.mylog.system.service.SysArticleUpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 彭上尚
 * @description 针对表【sys_article_up(文章点赞记录表)】的数据库操作Service实现
 * @createDate 2025-04-02 22:01:57
 */
@Service
public class SysArticleUpServiceImpl extends ServiceImpl<SysArticleUpDao, SysArticleUp> implements SysArticleUpService {

    @Resource
    RedisCacheUtils redisCacheUtils;

    @Resource
    SysArticleDao sysArticleDao;

    @Override
    public Boolean upArticle(Long articleId) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        Integer isUpRecord = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_UP_RECORD + userId + ":" + articleId);
        if (StringUtils.isNull(isUpRecord) || isUpRecord == 0) {
            redisCacheUtils.incrementValue(RedisConstants.REDIS_ARTICLE_UP_NUM + articleId);
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_UP_RECORD + userId + ":" + articleId, 1, 25, TimeUnit.HOURS);
        }
        return true;
    }

    @Override
    public Boolean noArticle(Long articleId) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        Integer isUpRecord = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_UP_RECORD + userId + ":" + articleId);
        if (StringUtils.isNotNull(isUpRecord) && isUpRecord == 1) {
            redisCacheUtils.decrement(RedisConstants.REDIS_ARTICLE_UP_NUM + articleId);
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_UP_RECORD + userId + ":" + articleId, 0, 25, TimeUnit.HOURS);
        }
        return true;
    }

    @Override
    public Integer getUpNum(Long articleId) {
        Integer num = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_UP_NUM + articleId);
        if (StringUtils.isNull(num)) {
            SysArticle sysArticle = sysArticleDao.selectById(articleId);
            if (StringUtils.isNotNull(sysArticle)) {
                num = sysArticle.getUpNum();
                if (StringUtils.isNull(num)) {
                    num = 0;
                }
            } else {
                num = 0;
            }
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_UP_NUM + articleId, num);
        }
        return num;
    }

    @Override
    public Boolean getIsUp(Long articleId, Long userId) {
        Integer isUpRecord = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_UP_RECORD + userId + ":" + articleId);
        Boolean isUp = false;
        if (StringUtils.isNull(isUpRecord)) {
            QueryWrapper<SysArticleUp> wq = new QueryWrapper<>();
            wq.eq("articleId", articleId);
            wq.eq("createBy", userId);
            long count = this.count(wq);
            if (count > 0) {
                isUp = true;
            } else {
                isUp = false;
            }
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_UP_RECORD + userId + ":" + articleId, isUp ? 1 : 0, 25, TimeUnit.HOURS);
        } else if (isUpRecord == 0) {
            isUp = false;
        } else {
            isUp = true;
        }
        return isUp;
    }
}




