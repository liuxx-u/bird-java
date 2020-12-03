package com.bird.lock.aspect;

import com.bird.lock.IDistributedLock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuxx
 * @since 2020/12/2
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 分布式锁的Key，支持Spel表达式
     */
    String key();

    /**
     * 锁过期时间，默认：1分钟
     */
    long keyExpire() default IDistributedLock.DEFAULT_KEY_EXPIRE;

    /**
     * 重试周期，默认：1秒
     */
    long retryInterval() default IDistributedLock.DEFAULT_RETRY_INTERVAL;

    /**
     * 最长重试时间，默认：1分钟
     */
    long retryExpire() default IDistributedLock.DEFAULT_RETRY_EXPIRE;
}
