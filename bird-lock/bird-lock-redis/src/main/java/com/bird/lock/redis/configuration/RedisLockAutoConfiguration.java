package com.bird.lock.redis.configuration;

import com.bird.lock.IDistributedLock;
import com.bird.lock.configuration.DistributeLockAutoConfiguration;
import com.bird.lock.redis.RedisDistributedLock;
import com.bird.lock.redis.watchdog.RedisLockWatchDog;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author liuxx
 * @since 2020/10/26
 */
@Configuration
@EnableConfigurationProperties(RedisLockProperties.class)
@ConditionalOnBean(value = StringRedisTemplate.class)
@AutoConfigureBefore(DistributeLockAutoConfiguration.class)
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration")
public class RedisLockAutoConfiguration {

    private final RedisLockProperties redisLockProperties;

    public RedisLockAutoConfiguration(RedisLockProperties redisLockProperties) {
        this.redisLockProperties = redisLockProperties;
    }

    @Bean
    @ConditionalOnMissingBean(IDistributedLock.class)
    public IDistributedLock distributedLock(StringRedisTemplate stringRedisTemplate, ObjectProvider<RedisLockWatchDog> watchDog) {
        return new RedisDistributedLock(stringRedisTemplate, redisLockProperties, watchDog.getIfAvailable());
    }

    @Bean
    @ConditionalOnProperty(value = "bird.lock.redis.watchdog.enabled", havingValue = "true", matchIfMissing = true)
    public RedisLockWatchDog redisLockWatchDog(StringRedisTemplate stringRedisTemplate) {
        return new RedisLockWatchDog(stringRedisTemplate, redisLockProperties);
    }
}
