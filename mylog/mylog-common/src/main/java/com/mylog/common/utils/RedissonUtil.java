package com.mylog.common.utils;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonUtil {

    @Autowired
    private RedissonClient redissonClient; // RedissonClient已经由配置类生成，这里自动装配即可

    /**
     * 锁住不设置超时时间(拿不到lock就不罢休，不然线程就一直block)
     *
     * @param lockKey
     * @return org.redisson.api.RLock
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getFairLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * leaseTime为加锁时间，单位为秒
     *
     * @param lockKey
     * @param leaseTime
     * @return org.redisson.api.RLock
     */
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getFairLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return null;
    }

    /**
     * timeout为加锁时间，时间单位由unit确定
     *
     * @param lockKey
     * @param unit
     * @param timeout
     * @return org.redisson.api.RLock
     */
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = redissonClient.getFairLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param unit
     * @param waitTime
     * @param leaseTime
     * @return boolean
     */
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 通过lockKey解锁
     *
     * @param lockKey
     * @return void
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getFairLock(lockKey);
        lock.unlock();
    }

    public void isUnlock(RLock lock) {
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.forceUnlock();
        }
    }

    /**
     * 直接通过锁解锁
     *
     * @param lock
     * @return void
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }

}
