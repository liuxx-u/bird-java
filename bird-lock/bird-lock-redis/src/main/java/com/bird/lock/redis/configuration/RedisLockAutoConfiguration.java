package com.bird.lock.redis.configuration;

import com.bird.lock.DistributedLockTemplate;
import com.bird.lock.IDistributedLock;
import com.bird.lock.redis.RedisDistributedLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author liuxx
 * @since 2020/10/26
 */
@Configuration
@ConditionalOnProperty(value = "spring.redis.host")
@EnableConfigurationProperties(RedisLockProperties.class)
@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration")
public class RedisLockAutoConfiguration {

    private final RedisLockProperties redisLockProperties;

    public RedisLockAutoConfiguration(RedisLockProperties redisLockProperties) {
        this.redisLockProperties = redisLockProperties;
    }

    @Bean
    @ConditionalOnMissingBean(IDistributedLock.class)
    public IDistributedLock distributedLock(StringRedisTemplate stringRedisTemplate) {
        return new RedisDistributedLock(stringRedisTemplate, redisLockProperties);
    }

    @Bean
    @ConditionalOnMissingBean(DistributedLockTemplate.class)
    public DistributedLockTemplate distributedLockTemplate(IDistributedLock distributedLock){
        return new DistributedLockTemplate(distributedLock);
    }
}
