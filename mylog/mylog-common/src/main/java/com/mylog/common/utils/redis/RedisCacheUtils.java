package com.mylog.common.utils.redis;

import com.mylog.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 *
 * @author pss
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCacheUtils {
    @Autowired
    public RedisTemplate redisTemplate;

    //        public static final String SETNX_SCRIPT = "return redis.call('setnx',KEYS[1], ARGV[1])";
    public static final String SETNX_SCRIPT = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then\n" + "    return redis.call('expire',KEYS[1],ARGV[2])\n" + "else\n" + "    return -1000\n" + "end";
    // 定义 Lua 脚本（类常量）
//    public static final String SETNX_WITH_EXPIRE_SCRIPT = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then\n" + "   if tonumber(ARGV[2]) > 0 then\n" + "       redis.call(ARGV[3], KEYS[1], ARGV[2])\n" + "   end\n" + "   return 1\n" + "else\n" + "   return 0\n" + "end";


    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        try {
            if (StringUtils.isNotBlank(key)) {
                return redisTemplate.hasKey(new String(key.getBytes("UTF-8")));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public <T> void setCacheObjectForever(final String key, final T value) {
        redisTemplate.opsForValue().set(new String(key.getBytes(StandardCharsets.UTF_8)), value);
    }


    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(new String(key.getBytes(StandardCharsets.UTF_8)), value, 30, TimeUnit.MINUTES);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(new String(key.getBytes(StandardCharsets.UTF_8)), value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(new String(key.getBytes(StandardCharsets.UTF_8)), timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(new String(key.getBytes(StandardCharsets.UTF_8)), timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(new String(key.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(new String(key.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        return this.setCacheList(key, dataList, 60 * 60 * 24, TimeUnit.SECONDS);
    }

    public <T> long setCacheList(final String key, final List<T> dataList, final long timeout) {
        return this.setCacheList(key, dataList, timeout, TimeUnit.SECONDS);
    }

    public <T> long setCacheList(final String key, final List<T> dataList, final long timeout, final TimeUnit unit) {
        Long count = null;
        count = redisTemplate.opsForList().rightPushAll(new String(key.getBytes(StandardCharsets.UTF_8)), dataList);
        if (StringUtils.isNotNull(count)) {
            this.expire(key, timeout, unit);
        }
        return count == null ? 0 : count;
    }


    /**
     * 往右边插入数据
     *
     * @param key   缓存的键值
     * @param value 数据
     * @return 缓存的对象
     */
    public <T> long setCacheRightPushList(final String key, final T value) {
        Boolean b = redisTemplate.hasKey(key);
        Long count = null;
        if (!b) {
            count = this.setCacheList(key, Arrays.asList(value), 60 * 60 * 24, TimeUnit.SECONDS);
        } else {

            count = redisTemplate.opsForList().rightPush(new String(key.getBytes(StandardCharsets.UTF_8)), value);
        }
        return count == null ? 0 : count;
    }

    public <T> long setCacheRightPushList(final String key, final List<T> value) {
        Boolean b = redisTemplate.hasKey(key);
        Long count = null;
        if (!b) {
            count = this.setCacheList(key, value, 60 * 60 * 24, TimeUnit.SECONDS);
        } else {
            value.forEach(i -> this.setCacheRightPushList(key, i));
        }
        return count == null ? 0 : count;
    }

    /**
     * 设置指定下标的值
     *
     * @param key
     * @param index
     * @param value
     * @param <T>
     */

    public <T> void setCacheSetList(final String key, final long index, final T value) {
        Boolean b = redisTemplate.hasKey(key);
        if (b) {
            redisTemplate.opsForList().set(key, index, value);
        }
    }

    /**
     * 通过下标获取元素
     *
     * @param key
     * @param index
     * @param <T>
     */
    public <T> Object getCacheIndexList(final String key, final long index) {
        Boolean b = redisTemplate.hasKey(key);
        if (b) {
            return redisTemplate.opsForList().index(key, index);
        }
        return null;
    }

    /**
     * 获取list的长度
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long getCacheListSize(final String key) {
        // 检查Redis中是否存在指定的key
        Boolean b = redisTemplate.hasKey(key);
        // 如果key存在
        if (b) {
            // 返回与该key关联的列表的长度
            return redisTemplate.opsForList().size(key);
        }
        // 如果key不存在，返回null
        return null;
    }


    /**
     * 往左边插入数据
     *
     * @param key   缓存的键值
     * @param value 数据
     * @return 缓存的对象
     */
    public <T> long setCacheLeftPushList(final String key, final T value) {
        Boolean b = redisTemplate.hasKey(key);
        Long count = null;
        if (!b) {
            count = this.setCacheList(key, Collections.singletonList(value), 60 * 60 * 24, TimeUnit.SECONDS);
        } else {
            count = redisTemplate.opsForList().leftPush(new String(key.getBytes(StandardCharsets.UTF_8)), value);
        }
        return count == null ? 0 : count;
    }


    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(new String(key.getBytes(StandardCharsets.UTF_8)), 0, -1);
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key, long start, long end) {
        return redisTemplate.opsForList().range(new String(key.getBytes(StandardCharsets.UTF_8)), start, end);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = null;
        setOperation = redisTemplate.boundSetOps(new String(key.getBytes(StandardCharsets.UTF_8)));
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }


    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(new String(key.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 缓存zSet
     *
     * @param key   缓存键值
     * @param value 缓存的数据
     * @return 缓存数据的对象
     */
    public Boolean setCacheZSet(final String key, final Object value, final double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取指定分数区间的元素
     *
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public <T> Set<T> getZSetByScore(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    /**
     * desc
     *
     * @param key
     * @param minScore
     * @param maxScore
     * @param <T>
     * @return
     */
    public <T> Set<T> getZSetByRange(String key, long minScore, long maxScore) {
        return redisTemplate.opsForZSet().reverseRange(key, minScore, maxScore);
    }

    /**
     * 获取 ZSet 中的所有元素
     *
     * @param key
     * @return
     */
    public <T> Set<T> getAllZSet(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }

    public Boolean zsetAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    public Long zsetRemove(String key, Object... value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }

    public Long zsetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(new String(key.getBytes(StandardCharsets.UTF_8)), dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(new String(key.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(new String(key.getBytes(StandardCharsets.UTF_8)), hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(new String(key.getBytes(StandardCharsets.UTF_8)), hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key
     * @param
     */
    public void delCacheMapValue(final String key, final String hkey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(new String(key.getBytes(StandardCharsets.UTF_8)), hkey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(new String(key.getBytes(StandardCharsets.UTF_8)), hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    public Long incrementValue(String key) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        // 直接加一，适用于存储为整数的场景
        return valueOps.increment(key, 1);
    }

    public void decrement(String key) {
        this.decreaseIfNotNegative(key);
    }

    public void decreaseIfNotNegative(String key) {
        String script = "local key = KEYS[1]\n" + "local value = tonumber(redis.call('GET', key))\n" + "if value > 0 then\n" + "    redis.call('DECR', key)\n" + "end";
        DefaultRedisScript<Void> redisScript = new DefaultRedisScript<>(script, Void.class);
        redisTemplate.execute(redisScript, Collections.singletonList(key));
    }


    /**
     * 执行lua脚本
     *
     * @param script
     * @param keys
     * @param args
     * @param <T>
     * @return
     */
    public <T> T execute(RedisScript<T> script, List<T> keys, Object... args) {
        return (T) redisTemplate.execute(script, keys, args);
    }

    /**
     * 原子性 setnx 操作并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间值
     * @return 是否设置成功
     */
    public Boolean setNX(String key, Object value, long timeout) {
        String luaScript = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then " + "return redis.call('expire', KEYS[1], ARGV[2]) " + "end return 0";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(key), value, timeout);
        return result != null && result == 1;
    }

    /**
     * HyperLogLog 添加元素
     *
     * @param key
     * @param value
     * @return
     */
    public Long addHyperLog(String key, String value) {
        return redisTemplate.opsForHyperLogLog().add(key, value);
    }

    /**
     * 获取 HyperLogLog 的元素数量
     *
     * @param key
     * @return
     */
    public Long getHyperLogCount(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    public List<Object> pipeline(SessionCallback<Object> sessionCallback) {
        return redisTemplate.executePipelined(sessionCallback);
    }
}
