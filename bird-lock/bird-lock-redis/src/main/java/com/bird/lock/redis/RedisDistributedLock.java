package com.bird.lock.redis;

import com.bird.lock.AbstractDistributedLock;
import com.bird.lock.reentrant.ILockReentrance;
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

    private static final String DEFAULT_LOCK_PREFIX = "bird:lock:";

    private StringRedisTemplate redisTemplate;

    private String lockPrefix = DEFAULT_LOCK_PREFIX;

    public RedisDistributedLock(StringRedisTemplate redisTemplate, ILockReentrance lockReentrance) {
        super(lockReentrance);

        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean tryLock(String lockKey, String lockValue, int expire) {
        // 尝试加锁
        return redisTemplate.opsForValue().setIfAbsent(lockPrefix + lockKey, lockValue, expire, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean releaseLock(String lockKey, String lockValue) {
        String key = lockPrefix + lockKey;
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
