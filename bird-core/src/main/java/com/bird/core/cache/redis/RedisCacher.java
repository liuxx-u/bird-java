package com.bird.core.cache.redis;

import com.bird.core.cache.Cacher;
import com.bird.core.utils.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuxx on 2017/5/16.
 */
@Component
public final class RedisCacher implements Cacher {

    @Autowired
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    @Value("${redis.expiration}")
    private Integer EXPIRE;

    // 获取连接
    @SuppressWarnings("unchecked")
    private RedisTemplate<Serializable, Serializable> getRedis() {
        if (redisTemplate == null) {
            synchronized (RedisCacher.class) {
                if (redisTemplate == null) {
                    redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
                }
            }
        }
        return redisTemplate;
    }

    public final Object get(final String key) {
        return getRedis().boundValueOps(key).get();
    }

    public final Set<Object> getAll(final String pattern) {
        Set<Object> values = new HashSet<>();
        Set<Serializable> keys = getRedis().keys(pattern);
        for (Serializable key : keys) {
            values.add(getRedis().opsForValue().get(key));
        }
        return values;
    }

    public final void set(final String key, final Serializable value, int seconds) {
        getRedis().boundValueOps(key).set(value);
        expire(key, seconds);
    }

    public final void set(final String key, final Serializable value) {
        getRedis().boundValueOps(key).set(value);
        expire(key, EXPIRE);
    }

    public final Boolean exists(final String key) {
        return getRedis().hasKey(key);
    }

    public final void del(final String key) {
        getRedis().delete(key);
    }

    public final void delAll(final String pattern) {
        getRedis().delete(getRedis().keys(pattern));
    }

    public final String type(final String key) {
        return getRedis().type(key).getClass().getName();
    }

    /**
     * 在某段时间后失效
     *
     * @return
     */
    public final Boolean expire(final String key, final int seconds) {
        return getRedis().expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 在某个时间点失效
     *
     * @param key
     * @param unixTime
     * @return
     */
    public final Boolean expireAt(final String key, final long unixTime) {
        return getRedis().expireAt(key, new Date(unixTime));
    }

    public final Long ttl(final String key) {
        return getRedis().getExpire(key, TimeUnit.SECONDS);
    }

    public final void setrange(final String key, final long offset, final String value) {
        getRedis().boundValueOps(key).set(value, offset);
        expire(key, EXPIRE);
    }

    public final String getrange(final String key, final long startOffset, final long endOffset) {
        return getRedis().boundValueOps(key).get(startOffset, endOffset);
    }

    public final Object getSet(final String key, final Serializable value) {
        return getRedis().boundValueOps(key).getAndSet(value);
    }

    public boolean setnx(String key, Serializable value) {
        return getRedis().boundValueOps(key).setIfAbsent(value);
    }

    public void unlock(String key) {
        del(key);
    }

    public void hset(String key, String field, String value) {
        getRedis().boundHashOps(key).put(field, value);
    }

    public Object hget(String key, String field) {
        return getRedis().boundHashOps(key).get(field);
    }

    public void hdel(String key, String field) {
        getRedis().boundHashOps(key).delete(field);
    }
}

