package com.bird.lock.redis.configuration;

import com.bird.lock.redis.watchdog.WatchdogProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
     * 锁过期时间，默认：10秒
     */
    private Long keyExpire = 10000L;
    /**
     * 获取锁失败时重试周期，默认：1秒
     */
    private Long retryInterval = 1000L;
    /**
     * tryLock失败时最大重试时间，默认：30秒
     */
    private Long retryExpire = 30000L;
    /**
     * 看门狗配置
     */
    @NestedConfigurationProperty
    private WatchdogProperties watchdog = new WatchdogProperties();
}
