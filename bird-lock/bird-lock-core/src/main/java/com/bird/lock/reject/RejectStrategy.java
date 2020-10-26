package com.bird.lock.reject;

/**
 * @author liuxx
 * @since 2020/10/22
 */
public interface RejectStrategy<T> {

    /**
     * 拒绝获取锁
     * @param lockKey 要获取锁的key
     * @return 返回值
     */
    T reject(String lockKey);
}
