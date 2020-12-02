package com.bird.lock.redis.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/12/2
 */
@Data
@ConfigurationProperties(value = "bird.lock.redis")
public class RedisLockProperties {

    /**
     * 分布式锁
     */
    private String keyPrefix = "bird:lock:";
    /**
     * 锁过期时间，默认：1分钟
     */
    private Long keyExpire = 60000L;
    /**
     * 获取锁失败时重试周期，默认：1秒
     */
    private Long retryInterval = 1000L;
    /**
     * tryLock失败时最大重试时间，默认：1分钟
     */
    private Long retryExpire = 60000L;
}
