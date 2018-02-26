package com.bird.core.cache.redis;

import com.bird.core.cache.redis.jedis.JedisShardInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author liuxx
 * @date 2018/2/26
 */
@Configuration

public class RedisConfig {
    @Value("${redis.host:127.0.0.1}")
    private String host;
    @Value("${redis.password:password}")
    private String password;
    @Value("${redis.port:6739}")
    private int port;
    @Value("${redis.minIdle:5}")
    private int minIdle;
    @Value("${redis.maxIdle:20}")
    private int maxIdle;
    @Value("${redis.maxTotal:100}")
    private int maxTotal;
    @Value("${redis.expiration:600}")
    private int expiration;
    @Value("${redis.maxWaitMillis:5000}")
    private int maxWaitMillis;

    @Bean
    public JedisShardInfo jedisShardInfo() {
        JedisShardInfo info = new JedisShardInfo(host, port);
        info.setPassword(password);
        return info;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(true);

        return config;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig poolConfig,JedisShardInfo shardInfo){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(poolConfig);
        factory.setShardInfo(shardInfo);
        return factory;
    }

    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory connectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }
}
