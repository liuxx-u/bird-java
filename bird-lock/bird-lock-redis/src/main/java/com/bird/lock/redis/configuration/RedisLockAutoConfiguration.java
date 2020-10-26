package com.bird.lock.redis.configuration;

import com.bird.lock.IDistributedLock;
import com.bird.lock.redis.RedisDistributedLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author liuxx
 * @since 2020/10/26
 */
@Configuration
@ConditionalOnProperty(value = "spring.redis.host")
@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration")
public class RedisLockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IDistributedLock.class)
    public IDistributedLock distributedLock(StringRedisTemplate stringRedisTemplate){
        return new RedisDistributedLock(stringRedisTemplate);
    }
}
