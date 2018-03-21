package com.bird.core.cache.redis;

import com.bird.core.cache.ICacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuxx on 2017/5/16.
 */
@Component
public final class RedisCacher implements ICacher {

    @Value("${spring.redis.expiration}")
    private Integer EXPIRE;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public final Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public final void set(final String key, final Serializable value, int seconds) {
        redisTemplate.opsForValue().set(key,value);
        expire(key, seconds);
    }

    @Override
    public final void set(final String key, final Serializable value) {
        redisTemplate.opsForValue().set(key,value,EXPIRE,TimeUnit.SECONDS);
        expire(key, EXPIRE);
    }

    @Override
    public Boolean setIfAbsent(String key, Serializable value) {
        return redisTemplate.opsForValue().setIfAbsent(key,value);
    }

    @Override
    public final Boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public final void del(final String key) {
        redisTemplate.delete(key);
    }

    /**
     * 在某段时间后失效
     *
     * @return
     */
    public final Boolean expire(final String key, final int seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 在某个时间点失效
     *
     * @param key
     * @param unixTime
     * @return
     */
    @Override
    public final Boolean expireAt(final String key, final long unixTime) {
        return redisTemplate.expireAt(key, new Date(unixTime));
    }
}

