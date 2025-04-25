package com.mylog.system.jop.cycle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mylog.common.enums.UpTypeEnum;
import com.mylog.common.utils.redis.RedisKeyUtil;
import com.mylog.system.entity.SysArticleUp;
import com.mylog.system.service.SysArticleUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author pss
 * @date 2025/4/2
 */
@Component
@Slf4j
public class IncSyncArticleUpRecord {

    @Resource
    SysArticleUpService sysArticleUpService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    //    @Scheduled(cron = "0 15 0 * * ? ")
    @Scheduled(fixedRate = 10000)
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        log.info("开始执行");
        DateTime nowDate = DateUtil.date();
        String date = DateUtil.format(nowDate, "HH:mm:") + (DateUtil.second(nowDate) / 10 - 1) * 10;
        syncUp2DBByDate(date);
        log.info("临时数据同步完成");
    }



    public void syncUp2DBByDate(String date) {
        // 获取到临时点赞和取消点赞数据
        // todo 如果数据量过大，可以分批读取数据
        String tempUpKey = RedisKeyUtil.getTempUpKey(date);
        Map<Object, Object> allTempUpMap = redisTemplate.opsForHash().entries(tempUpKey);
        boolean UpMapEmpty = CollUtil.isEmpty(allTempUpMap);
        // 同步 点赞 到数据库
        if (UpMapEmpty) {
            return;
        }
        ArrayList<SysArticleUp> upList = new ArrayList<>();
        LambdaQueryWrapper<SysArticleUp> wrapper = new LambdaQueryWrapper<>();
        boolean needRemove = false;
        for (Object userIdBlogIdObj : allTempUpMap.keySet()) {
            String userIdBlogId = (String) userIdBlogIdObj;
            String[] userIdAndBlogId = userIdBlogId.split(StrPool.COLON);
            Long userId = Long.valueOf(userIdAndBlogId[0]);
            Long articleId = Long.valueOf(userIdAndBlogId[1]);
            // -1 取消点赞，1 点赞
            Integer UpType = Integer.valueOf(allTempUpMap.get(userIdBlogId).toString());
            if (UpType == UpTypeEnum.INCR.getValue()) {
                SysArticleUp up = new SysArticleUp();
                up.setCreateBy(userId);
                up.setArticleId(articleId);
                upList.add(up);
            } else if (UpType == UpTypeEnum.DECR.getValue()) {
                // 拼接查询条件，批量删除
                // todo 数据量过大，可以分批操作
                needRemove = true;
                wrapper.or().eq(SysArticleUp::getCreateBy, userId).eq(SysArticleUp::getArticleId, articleId);
            } else {
                if (UpType != UpTypeEnum.NON.getValue()) {
                    log.warn("数据异常：{}", userId + "," + articleId + "," + UpType);
                }
            }
        }
        // 批量插入
        sysArticleUpService.saveBatch(upList);
        // 批量删除
        if (needRemove) {
            sysArticleUpService.remove(wrapper);
        }
        redisTemplate.delete(tempUpKey);
    }

}
