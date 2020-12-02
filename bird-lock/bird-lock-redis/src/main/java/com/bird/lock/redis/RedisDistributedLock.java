package com.bird.lock.redis;

import com.bird.lock.AbstractDistributedLock;
import com.bird.lock.redis.configuration.RedisLockProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 * @since 2020/10/26
 */
@Slf4j
public class RedisDistributedLock  extends AbstractDistributedLock {

    private final StringRedisTemplate redisTemplate;
    private final RedisLockProperties redisLockProperties;

    public RedisDistributedLock(StringRedisTemplate redisTemplate, RedisLockProperties redisLockProperties) {
        this.redisTemplate = redisTemplate;
        this.redisLockProperties = redisLockProperties;
    }

    @Override
    public void lock(String lockKey) {
        this.lock(lockKey, this.redisLockProperties.getKeyExpire());
    }

    @Override
    public void lock(String lockKey, long keyExpire) {
        super.lock(lockKey, keyExpire, this.redisLockProperties.getRetryInterval());
    }

    @Override
    public boolean tryLock(String lockKey) {
        return this.tryLock(lockKey, this.redisLockProperties.getKeyExpire());
    }

    @Override
    public boolean tryLock(String lockKey, long keyExpire) {
        return this.tryLock(lockKey, keyExpire, this.redisLockProperties.getRetryExpire());
    }

    @Override
    public boolean tryLock(String lockKey, long keyExpire, long retryExpire) {
        return super.tryLock(lockKey, keyExpire, this.redisLockProperties.getRetryInterval(), retryExpire);
    }

    @Override
    public boolean tryLock(String lockKey, String lockValue, long expire) {
        // 尝试加锁
        return redisTemplate.opsForValue().setIfAbsent(this.redisLockProperties.getKeyPrefix() + lockKey, lockValue, expire, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean releaseLock(String lockKey, String lockValue) {
        String key = this.redisLockProperties.getKeyPrefix() + lockKey;
        // 获取锁的值
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.equals(value, lockValue)) {
            // 如果值匹配, 释放锁
            //TODO：如果此时锁过期并被其他线程获取到锁，会出现误解锁
            return redisTemplate.delete(key);
        }
        // 值不匹配, 可能是锁过期被其他人获取了, 此时不允许删除其他人获取的锁
        log.warn("release lock failed, key:{}, current value {} does't match lock value {}", key, lockValue, value);
        return false;
    }
}
