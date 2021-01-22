package com.bird.websocket.common.configuration;

import com.bird.websocket.common.ITokenSessionStorage;
import com.bird.websocket.common.ITokenUserStorage;
import com.bird.websocket.common.IUserTokensStorage;
import com.bird.websocket.common.storage.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author yuanjian
 */
@Configuration
@EnableConfigurationProperties({WebSocketProperties.class})
@ConditionalOnMissingBean({IUserTokensStorage.class, ITokenUserStorage.class, ITokenSessionStorage.class})
public class WebSocketStorageAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(RedisAutoConfiguration.class)
    @ConditionalOnProperty(name = "bird.websocket.storage", havingValue = "REDIS")
    static class RedisStorage {

        @Bean
        public IUserTokensStorage redisUserTokensStorage(StringRedisTemplate stringRedisTemplate) {
            return new RedisUserTokensStorage(stringRedisTemplate);
        }

        @Bean
        public ITokenUserStorage redisTokenUserStorage(StringRedisTemplate stringRedisTemplate) {
            return new RedisTokenUserStorage(stringRedisTemplate);
        }

        @Bean
        public ITokenSessionStorage redisTokenSessionStorage(RedisTemplate<Object, Object> redisTemplate) {
            return new RedisTokenSessionStorage(redisTemplate);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(RedisStorage.class)
    @ConditionalOnProperty(name = "bird.websocket.storage", havingValue = "INTERNAL", matchIfMissing = true)
    static class InternalStorage {

        @Bean
        public IUserTokensStorage internalUserTokensStorage() {
            return new InternalUserTokensStorage();
        }

        @Bean
        public ITokenUserStorage internalTokenUserStorage() {
            return new InternalTokenUserStorage();
        }

        @Bean
        public ITokenSessionStorage internalTokenSessionStorage() {
            return new InternalTokenSessionStorage();
        }
    }
}
