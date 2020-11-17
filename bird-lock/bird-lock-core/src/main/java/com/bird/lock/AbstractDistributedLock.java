package com.bird.lock;

import com.bird.lock.reentrant.ILockReentrance;
import com.bird.lock.reject.RejectStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * @author liuxx
 * @since 2020/10/22
 */
@Slf4j
public abstract class AbstractDistributedLock implements IDistributedLock {

    private final ILockReentrance lockReentrance;

    public AbstractDistributedLock(ILockReentrance lockReentrance){
        this.lockReentrance = lockReentrance;
    }

    @Override
    public <T> T withLock(String lockKey, Supplier<T> supplier, int retry, int retryInterval, int expire, RejectStrategy<T> rejectStrategy) {
        if (StringUtils.isBlank(lockKey)) {
            throw new RuntimeException("lock key cannot be empty");
        }

        if(lockReentrance.reentry(lockKey)){
            return executeAndExitReentry(lockKey, supplier);
        }

        // 生成一个随机字符串, 用于加锁
        String lockValue = getLockValue();

        do {
            boolean locked = tryLock(lockKey, lockValue, expire);
            if (locked) {
                lockReentrance.initialize(lockKey);
                // 如果获取成功, 继续往下执行
                return executeAndRelease(lockKey, lockValue, supplier);
            }

            if (retry > 0) {
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException ex) {
                    log.error("", ex);
                }
            }
        } while (retry-- > 0);

        // 未获取到锁，执行拒绝策略
        return rejectStrategy.reject(lockKey);
    }

    @Override
    public <T> T withUnique(String lockKey, Supplier<T> query, Supplier<T> createFunc) {
        // 先尝试获取一次
        T value = query.get();
        if (value != null) {
            // 获取成功, 快速返回
            return value;
        }
        if (StringUtils.isBlank(lockKey)) {
            throw new RuntimeException("lock key cannot be empty");
        }
        // 加锁
        return withLock(lockKey, () -> {
            // 双重检查
            T val = query.get();
            if (val != null) {
                return val;
            }
            // 最终创建
            return createFunc.get();
        });
    }

    /**
     * 执行方法并退出重入锁
     * @param lockKey 锁的Key
     * @param supplier 执行方法体
     * @param <T> 返回参数类型
     * @return 返回值
     */
    private <T> T executeAndExitReentry(String lockKey, Supplier<T> supplier){
        try {
            return supplier.get();
        } finally {
            lockReentrance.exit(lockKey);
        }
    }

    /**
     * 执行方法并释放锁
     * @param lockKey 锁的Key
     * @param lockValue 锁的Value
     * @param supplier 执行方法体
     * @param <T> 返回参数类型
     * @return 返回值
     */
    private <T> T executeAndRelease(String lockKey, String lockValue, Supplier<T> supplier) {
        try {
            // 进行真正的执行
            return supplier.get();
        } finally {
            //清除线程中的重入状态信息
            lockReentrance.remove(lockKey);
            // 执行完毕, 释放锁
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
