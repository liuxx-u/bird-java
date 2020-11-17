package com.bird.lock.reentrant;

/**
 * @author liuxx
 * @since 2020/11/16
 */
public interface ILockReentrance {

    /**
     * 初始化
     * @param lockKey 锁的Key
     */
    void initialize(String lockKey);

    /**
     * 重入
     * @param lockKey 锁的Key
     * @return 是否重入成功
     */
    boolean reentry(String lockKey);

    /**
     * 退出
     * @param lockKey 锁的Key
     */
    void exit(String lockKey);

    /**
     * 移除锁的重入信息
     * @param lockKey 锁的Key
     */
    void remove(String lockKey);

    /**
     * 清除所有锁的重入信息
     */
    void clear();
}
