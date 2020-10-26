package com.bird.lock;

import com.bird.lock.reject.RejectStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * @author liuxx
 * @since 2020/10/22
 */
@Slf4j
public abstract class AbstractDistributedLock implements IDistributedLock {

    @Override
    public <T> T withLock(String lockKey, Supplier<T> supplier, int retry, int retryInterval, int expire, RejectStrategy<T> rejectStrategy) {
        if (lockKey == null) {
            throw new RuntimeException("lock key cannot be null");
        }
        // 生成一个随机字符串, 用于加锁
        String lockValue = getLockValue();
        // 尝试获取锁
        boolean locked = tryLock(lockKey, lockValue, expire);
        if (locked) {
            // 如果获取成功, 继续往下执行
            return executeAndRelease(lockKey, lockValue, supplier);
        } else {
            // 获取失败, 并且重试次数
            while (retry > 0 || retry == -1) {
                // 如果不是无限重试, 重试次数减1
                retry = retry == -1 ? retry : retry - 1;
                // 阻塞一个周期开始下一次尝试
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException ex) {
                    log.error("",ex);
                }
                // 开始尝试重试获取锁
                locked = tryLock(lockKey, lockValue, expire);
                if (locked) {
                    // 如果获取到锁, 则执行并释放
                    return executeAndRelease(lockKey, lockValue, supplier);
                }
            }
        }
        // 最终执行拒绝策略
        return rejectStrategy.reject(lockKey);
    }

    @Override
    public <T> T withUnique(String key, Supplier<T> query, Supplier<T> createFunc) {
        if (query == null) {
            throw new RuntimeException("query cannot be null");
        }
        // 先尝试获取一次
        T value = query.get();
        if (value != null) {
            // 获取成功, 快速返回
            return value;
        }
        if (key == null) {
            throw new RuntimeException("unique key cannot be null");
        }
        // 加锁
        return withLock(key, () -> {
            // 双重检查
            T val = query.get();
            if (val != null) {
                return val;
            }
            // 最终创建
            return createFunc.get();
        });
    }


    private <T> T executeAndRelease(String lockKey, String lockValue, Supplier<T> supplier) {
        try {
            // 进行真正的执行
            return supplier.get();
        } catch (Exception e) {
            // 如果有错, 释放锁, 然后抛出异常
            releaseLock(lockKey, lockValue);
            throw e;
        } finally {
            // 正常执行完毕, 释放锁
            releaseLock(lockKey, lockValue);
        }
    }

    private String getLockValue() {
        return System.currentTimeMillis() + "-" + ThreadLocalRandom.current().nextInt(100, 999);
    }

    /**
     * 尝试获取锁的方法, 不同的分布式锁实现方式实现该方法就可以实现分布式锁功能
     *
     * @param lockKey   锁的key
     * @param lockValue 锁的value
     * @param expire    锁的过期时间, 单位:毫秒
     * @return 是否加锁成功
     */
    protected abstract boolean tryLock(String lockKey, String lockValue, int expire);

    /**
     * 释放锁, 不同的分布式锁实现方式实现该方法就可以实现分布式锁功能
     *
     * @param lockKey   锁的key
     * @param lockValue 锁的value
     * @return 锁是否释放成功
     */
    protected abstract boolean releaseLock(String lockKey, String lockValue);
}
