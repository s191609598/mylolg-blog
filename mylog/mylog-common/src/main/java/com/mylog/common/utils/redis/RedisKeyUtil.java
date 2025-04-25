package com.mylog.common.utils.redis;

import com.mylog.common.constant.RedisConstants;

/**
 * @author pss
 * @date 2025/4/24
 */
public class RedisKeyUtil {
    public static String getUserUpKey(Long userId) {
        return RedisConstants.REDIS_ARTICLE_UP + userId;
    }

    /**
     * 获取 临时点赞记录 key
     */
    public static String getTempUpKey(String time) {
        return String.format(RedisConstants.REDIS_ARTICLE_PREFIX,time);
    }
}
