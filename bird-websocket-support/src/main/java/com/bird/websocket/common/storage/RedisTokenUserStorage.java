package com.bird.websocket.common.storage;

import com.bird.websocket.common.ITokenUserStorage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Token - 用户 映射 Redis存储
 *
 * @author yuanjian
 */
public class RedisTokenUserStorage implements ITokenUserStorage {

    private static final String TOKEN_USER_PREFIX = "bird.token_user.";
    private final StringRedisTemplate stringRedisTemplate;

    public RedisTokenUserStorage(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String get(String token) {
        return stringRedisTemplate.opsForValue().get(adapterKey(token));
    }

    @Override
    public boolean put(String token, String userId) {
        stringRedisTemplate.opsForValue().set(adapterKey(token), userId);
        return true;
    }

    @Override
    public synchronized String remove(String token) {
        String userId = this.get(token);
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        stringRedisTemplate.delete(adapterKey(token));
        return userId;
    }

    @Override
    public boolean contain(String token) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(adapterKey(token)));
    }

    public String adapterKey(String token) {
        return TOKEN_USER_PREFIX + token;
    }

}
