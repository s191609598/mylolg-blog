package com.mylog.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.enums.LuaStatusEnum;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.common.utils.redis.RedisKeyUtil;
import com.mylog.common.utils.redis.RedisLuaScriptConstant;
import com.mylog.common.utils.redis.RedissonUtil;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysArticleDao;
import com.mylog.system.dao.SysArticleUpDao;
import com.mylog.system.entity.SysArticleUp;
import com.mylog.system.entity.article.SysArticle;
import com.mylog.system.service.SysArticleUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author 彭上尚
 * @description 针对表【sys_article_up(文章点赞记录表)】的数据库操作Service实现
 * @createDate 2025-04-02
 */
@Service
public class SysArticleUpServiceImpl extends ServiceImpl<SysArticleUpDao, SysArticleUp> implements SysArticleUpService {

    @Resource
    RedisCacheUtils redisCacheUtils;

    @Resource
    SysArticleDao sysArticleDao;

    @Resource
    RedissonUtil redissonUtil;

    @Autowired
    public RedisTemplate redisTemplate;


    @Override
    public Boolean upArticle(Long articleId) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());

        String timeSlice = getTimeSlice();
        // Redis Key
        String tempUpKey = RedisKeyUtil.getTempUpKey(timeSlice);
        String userUpKey = RedisKeyUtil.getUserUpKey(userId);

        // 执行 Lua 脚本
        Long result = (Long) redisTemplate.execute(
                RedisLuaScriptConstant.THUMB_SCRIPT,
                Arrays.asList(tempUpKey, userUpKey),
                userId,
                articleId
        );
        AssertUtils.assertIf(LuaStatusEnum.FAIL.getValue() == result, "用户已点赞");
        redisCacheUtils.incrementValue(RedisConstants.REDIS_ARTICLE_UP_NUM + articleId);
        return true;
    }

    @Override
    public Boolean noArticle(Long articleId) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        // 计算时间片
        String timeSlice = getTimeSlice();
        // Redis Key
        String tempUpKey = RedisKeyUtil.getTempUpKey(timeSlice);
        String userUpKey = RedisKeyUtil.getUserUpKey(userId);
        // 执行 Lua 脚本
        Long result = (Long) redisTemplate.execute(
                RedisLuaScriptConstant.UNTHUMB_SCRIPT,
                Arrays.asList(tempUpKey, userUpKey),
                userId,
                articleId
        );
        AssertUtils.assertIf(LuaStatusEnum.FAIL.getValue() == result, "用户未点赞");
        redisCacheUtils.decrement(RedisConstants.REDIS_ARTICLE_UP_NUM + articleId);
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
        String userUpKey = RedisKeyUtil.getUserUpKey(userId);
        Boolean isUp = false;
        String articleIdStr = String.valueOf(articleId);
        Object o = redisTemplate.opsForHash().get(userUpKey, articleIdStr);
        if (StringUtils.isNotNull(o)) {
            isUp = true;
        }
        return isUp;
    }

    @Override
    public Boolean deleteCollects(List<SysArticleUp> deletes) {
        if (StringUtils.isNotEmpty(deletes)) {
            UpdateWrapper<SysArticleUp> wq = new UpdateWrapper();
            wq.and(i -> {
                deletes.forEach(j -> {
                    i.and(k -> k.eq("articleId", j.getArticleId()).eq("createBy", j.getCreateBy()));
                });
            });
            this.remove(wq);
        }
        return true;
    }

    private String getTimeSlice() {
        DateTime nowDate = DateUtil.date();
        // 获取到当前时间前最近的整数秒，比如当前 11:20:23 ，获取到 11:20:20
        return DateUtil.format(nowDate, "HH:mm:") + (DateUtil.second(nowDate) / 10) * 10;
    }
}




