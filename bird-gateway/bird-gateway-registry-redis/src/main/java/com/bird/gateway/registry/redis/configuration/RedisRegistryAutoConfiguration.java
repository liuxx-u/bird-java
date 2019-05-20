package com.bird.gateway.registry.redis.configuration;

import com.bird.gateway.common.IRouteDiscovery;
import com.bird.gateway.common.IRouteRegistry;
import com.bird.gateway.common.RouteDefinition;
import com.bird.gateway.registry.redis.RedisRouteDiscovery;
import com.bird.gateway.registry.redis.RedisRouteRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author liuxx
 * @date 2019/5/20
 */
@Configuration
public class RedisRegistryAutoConfiguration {

    @Bean
    public RedisTemplate<String, RouteDefinition> routeRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, RouteDefinition> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setKeySerializer(keySerializer);

        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(IRouteRegistry.class)
    public RedisRouteRegistry redisRouteRegistry(RedisTemplate<String, RouteDefinition> redisTemplate){
        return new RedisRouteRegistry(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(IRouteDiscovery.class)
    public RedisRouteDiscovery redisRouteDiscovery(RedisTemplate<String, RouteDefinition> redisTemplate){
        return new RedisRouteDiscovery(redisTemplate);
    }
}
