package com.bird.lock;

import com.bird.lock.reject.ExceptionRejectStrategy;
import com.bird.lock.reject.RejectStrategy;

import java.util.function.Supplier;

/**
 * 分布式锁接口
 *
 * @author liuxx
 * @since 2020/10/22
 */
public interface IDistributedLock {

    /**
     * 默认的重试次数: 1次
     */
    int DEFAULT_RETRY = 1;
    /**
     * 默认的重试周期. 1秒
     */
    int DEFAULT_RETRY_INTERVAL = 1000;
    /**
     * 默认的过期时间: 1分钟,
     */
    int DEFAULT_EXPIRE = 60 * 1000;

    default <T> T withLock(String lockKey, Supplier<T> supplier) {
        return withLock(lockKey, supplier, DEFAULT_RETRY, DEFAULT_RETRY_INTERVAL, DEFAULT_EXPIRE, new ExceptionRejectStrategy<>());
    }

    default <T> T withLock(String lockKey, Supplier<T> supplier, RejectStrategy<T> rejectStrategy) {
        return withLock(lockKey, supplier, DEFAULT_RETRY, DEFAULT_RETRY_INTERVAL, DEFAULT_EXPIRE, rejectStrategy);
    }

    default <T> T withLock(String lockKey, Supplier<T> supplier, int expire) {
        return withLock(lockKey, supplier, DEFAULT_RETRY, DEFAULT_RETRY_INTERVAL, expire, new ExceptionRejectStrategy<>());
    }

    default <T> T withLock(String lockKey, Supplier<T> supplier, int retry, int retryInterval) {
        return withLock(lockKey, supplier, retry, retryInterval, DEFAULT_EXPIRE, new ExceptionRejectStrategy<>());
    }

    default <T> T justOne(String lockKey, Supplier<T> supplier) {
        return withLock(lockKey, supplier, 0, 0, DEFAULT_EXPIRE, new ExceptionRejectStrategy<>());
    }

    /**
     * 加锁执行
     *
     * @param lockKey        锁的key
     * @param supplier       要执行的函数
     * @param retry          重试次数
     * @param retryInterval  重试时间间隔
     * @param expire         锁过期时间
     * @param rejectStrategy 拒绝策略, 如果加锁失败, 并且超过重试次数的时候. 执行拒绝策略
     * @param <T>            返回类型
     * @return 执行结果
     */
    <T> T withLock(String lockKey, Supplier<T> supplier, int retry, int retryInterval, int expire, RejectStrategy<T> rejectStrategy);

    /**
     * 确保结果只有有且仅有一个
     *
     * @param key        加锁key
     * @param query      查询函数
     * @param createFunc 创建函数
     * @param <T>        返回结果
     * @return 查询到的或创建的结果
     */
    <T> T withUnique(String key, Supplier<T> query, Supplier<T> createFunc);
}
