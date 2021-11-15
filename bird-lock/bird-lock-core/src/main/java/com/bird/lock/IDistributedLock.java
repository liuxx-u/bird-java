package com.bird.lock;

/**
 * 分布式锁接口
 *
 * @author liuxx
 * @since 2020/10/22
 */
public interface IDistributedLock {

    /**
     * 默认的锁过期时间：10秒
     */
    int DEFAULT_KEY_EXPIRE = 10 * 1000;
    /**
     * 默认的重试周期：1秒
     */
    int DEFAULT_RETRY_INTERVAL = 1000;
    /**
     * 默认的最长重试时间: 30秒,
     */
    int DEFAULT_RETRY_EXPIRE = 30 * 1000;

    /**
     * 获取锁
     * <p>
     * 如果锁被其他方持有，一直重试直到获取锁成功，默认重试周期：1秒
     *
     * @param lockKey 锁的Key
     */
    default void lock(String lockKey) {
        lock(lockKey, DEFAULT_KEY_EXPIRE);
    }

    /**
     * 获取锁
     * <p>
     * 如果锁被其他方持有，一直重试直到获取锁成功，默认重试周期：1秒
     *
     * @param lockKey 锁的Key
     * @param keyExpire 锁过期时间
     */
    default void lock(String lockKey, long keyExpire) {
        lock(lockKey, keyExpire, DEFAULT_RETRY_INTERVAL);
    }

    /**
     * 获取锁
     * <p>
     * 如果锁被其他方持有，按指定的周期一直重试直到获取锁成功
     *
     * @param lockKey       锁的Key
     * @param keyExpire 锁过期时间
     * @param retryInterval 重试周期，单位：毫秒
     */
     default void lock(String lockKey, long keyExpire, long retryInterval) {
         tryLock(lockKey, keyExpire, retryInterval, Long.MAX_VALUE);
     }

    /**
     * 尝试获取锁
     *
     * 如果锁被其他方持有，在一分钟内进行重试，默认重试周期：1秒
     *
     * @param lockKey 锁的Key
     * @return 是否获取成功
     */
    default boolean tryLock(String lockKey) {
        return tryLock(lockKey, DEFAULT_KEY_EXPIRE, DEFAULT_RETRY_INTERVAL, DEFAULT_RETRY_EXPIRE);
    }

    /**
     * 尝试获取锁
     *
     * 如果锁被其他方持有，在一分钟内进行重试，默认重试周期：1秒
     *
     * @param lockKey 锁的Key
     * @param keyExpire 锁过期时间
     * @return 是否获取成功
     */
    default boolean tryLock(String lockKey, long keyExpire) {
        return tryLock(lockKey, keyExpire, DEFAULT_RETRY_INTERVAL, DEFAULT_RETRY_EXPIRE);
    }

    /**
     * 尝试获取锁
     *
     * 如果锁被其他方持有，在指定时间范围内进行重试，默认重试周期：1秒
     *
     * @param lockKey 锁的Key
     * @param keyExpire 锁过期时间
     * @param retryExpire    等待时间，单位：毫秒
     * @return 是否获取成功
     */
    default boolean tryLock(String lockKey, long keyExpire,long retryExpire) {
        return tryLock(lockKey, keyExpire, DEFAULT_RETRY_INTERVAL, retryExpire);
    }

    /**
     * 尝试获取锁
     *
     * 如果锁被其他方持有，按指定周期重试，在等待时间范围内获取锁成功则返回成功
     *
     * @param lockKey 锁的Key
     * @param keyExpire 锁过期时间
     * @param retryInterval    重试周期，单位：毫秒
     * @param retryExpire    等待时间，单位：毫秒
     * @return 是否获取成功
     */
    boolean tryLock(String lockKey, long keyExpire, long retryInterval, long retryExpire);

    /**
     * 释放锁
     *
     * @param lockKey 锁的Key
     */
    void unLock(String lockKey);
}
