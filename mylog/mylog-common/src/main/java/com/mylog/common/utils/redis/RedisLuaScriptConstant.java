package com.mylog.common.utils.redis;

import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * @author pss
 */
public class RedisLuaScriptConstant {

    /**
     * 点赞 Lua 脚本
     * KEYS[1]       -- 临时计数键
     * KEYS[2]       -- 用户点赞状态键
     * ARGV[1]       -- 用户 ID
     * ARGV[2]       -- 博客 ID
     * 返回:
     * -1: 已点赞
     * 1: 操作成功
     */
    public static final RedisScript<Long> THUMB_SCRIPT = new DefaultRedisScript<>("local tempThumbKey = KEYS[1] local userThumbKey = KEYS[2] local userId = ARGV[1] local blogId = ARGV[2] if redis.call('HEXISTS', userThumbKey, blogId) == 1 then\n" +
            "                return -1 end local hashKey = userId .. ':' .. blogId local oldNumber = tonumber(redis.call('HGET', tempThumbKey, hashKey) or 0) local newNumber = oldNumber + 1 redis.call('HSET', tempThumbKey, hashKey, newNumber) redis.call('HSET', userThumbKey, blogId, 1) return 1", Long.class);

    /**
     * 取消点赞 Lua 脚本
     * 参数同上
     * 返回：
     * -1: 未点赞
     * 1: 操作成功
     */
    public static final RedisScript<Long> UNTHUMB_SCRIPT = new DefaultRedisScript<>("local tempThumbKey = KEYS[1] local userThumbKey = KEYS[2] local userId = ARGV[1] local blogId = ARGV[2] if redis.call('HEXISTS', userThumbKey, blogId) ~= 1 then\n" +
            "                return -1 end local hashKey = userId .. ':' .. blogId local oldNumber = tonumber(redis.call('HGET', tempThumbKey, hashKey) or 0) local newNumber = oldNumber - 1 redis.call('HSET', tempThumbKey, hashKey, newNumber) redis.call('HDEL', userThumbKey, blogId) return 1", Long.class);

    /**
     * 点赞 Lua 脚本
     * KEYS[1]       -- 用户点赞状态键
     * ARGV[1]       -- 博客 ID
     * 返回:
     * -1: 已点赞
     * 1: 操作成功
     */
    public static final RedisScript<Long> THUMB_SCRIPT_MQ = new DefaultRedisScript<>("local userThumbKey = KEYS[1] local blogId = ARGV[1] if redis.call(\"HEXISTS\", userThumbKey, blogId) == 1 then\n" +
            "                        return -1\n" +
            "                    end redis.call(\"HSET\", userThumbKey, blogId, 1)\n" +
            "                    return 1", Long.class);

    /**
     * 取消点赞 Lua 脚本
     * KEYS[1]       -- 用户点赞状态键
     * ARGV[1]       -- 博客 ID
     * 返回:
     * -1: 已点赞
     * 1: 操作成功
     */
    public static final RedisScript<Long> UNTHUMB_SCRIPT_MQ = new DefaultRedisScript<>("local userThumbKey = KEYS[1] local blogId = ARGV[1] if redis.call(\"HEXISTS\", userThumbKey, blogId) == 0 then\n" +
            "                return -1\n" +
            "            end redis.call(\"HDEL\", userThumbKey, blogId)\n" +
            "            return 1", Long.class);
}
