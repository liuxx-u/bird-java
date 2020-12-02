package com.bird.lock;

import com.bird.lock.context.LockThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liuxx
 * @since 2020/10/22
 */
@Slf4j
public abstract class AbstractDistributedLock implements IDistributedLock {

    @Override
    public boolean tryLock(String lockKey, long keyExpire, long retryInterval, long retryExpire) {
        if (StringUtils.isBlank(lockKey)) {
            throw new RuntimeException("lock key cannot be empty");
        }

        if (LockThreadContext.reentry(lockKey)) {
            return true;
        }

        long start = System.currentTimeMillis();
        // 生成一个随机字符串, 用于加锁
        String lockValue = start + "-" + ThreadLocalRandom.current().nextInt(100, 999);

        long current = start;
        long end = start + retryExpire;
        do {
            if (this.tryLock(lockKey, lockValue, keyExpire)) {
                LockThreadContext.initialize(lockKey, lockValue);
                return true;
            }
            current += retryInterval;
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                log.warn("获取锁异常，线程中断", e);
            }
        } while (current <= end);

        return false;
    }

    @Override
    public void unLock(String lockKey) {
        LockThreadContext.LockValue lockValue = LockThreadContext.getLockValue(lockKey);
        if (lockValue == null) {
            log.warn("当前线程:{}不持有Key:{}对应的分布式锁", Thread.currentThread().getName(), lockKey);
            return;
        }
        if (lockValue.getReentrantCount() <= 0) {
            this.releaseLock(lockKey, lockValue.getLockValue());
        }

        LockThreadContext.exit(lockKey);
    }

    /**
     * 尝试获取锁的方法, 不同的分布式锁实现方式实现该方法就可以实现分布式锁功能
     *
     * @param lockKey   锁的key
     * @param lockValue 锁的value
     * @param keyExpire 锁的过期时间, 单位:毫秒
     * @return 是否加锁成功
     */
    protected abstract boolean tryLock(String lockKey, String lockValue, long keyExpire);

    /**
     * 释放锁, 不同的分布式锁实现方式实现该方法就可以实现分布式锁功能
     *
     * @param lockKey   锁的key
     * @param lockValue 锁的value
     * @return 锁是否释放成功
     */
    protected abstract boolean releaseLock(String lockKey, String lockValue);
}
