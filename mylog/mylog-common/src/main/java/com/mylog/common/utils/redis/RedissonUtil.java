package com.mylog.common.utils.redis;


import com.mylog.common.exception.MyException;
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
        return lock(lockKey, 3, 10);
    }

    public RLock lock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                return lock;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return lock;
    }

    public RLock lock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(waitTime, leaseTime, unit)) {
                return lock;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return lock;
    }

    public boolean lockb(String lockKey) {
        return this.lockb(lockKey, 3, 10);
    }

    public boolean lockb(String lockKey, long waitTime, long leaseTime) {
        return this.lockb(lockKey, waitTime, leaseTime, TimeUnit.SECONDS);
    }

    public boolean lockb(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            throw new MyException(e);
        }
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

    public boolean tryLock(String lockKey) {
        return this.tryLock(lockKey, TimeUnit.SECONDS, 3, 10);
    }

    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        return this.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
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
