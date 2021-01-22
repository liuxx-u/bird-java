package com.bird.websocket.common.storage;

import com.bird.websocket.common.IUserTokensStorage;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 用户 - Tokens 映射 Redis存储
 *
 * @author yuanjian
 */
public class RedisUserTokensStorage implements IUserTokensStorage {

    private static final String USER_TOKENS_PREFIX = "bird.user_tokens.";

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUserTokensStorage(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Set<String> get(String userId) {
        Set<String> tokens = stringRedisTemplate.opsForSet().members(adapterKey(userId));
        if (tokens == null) {
            tokens = new CopyOnWriteArraySet<>();
        }
        return tokens;
    }

    @Override
    public boolean add(String userId, String token) {
        stringRedisTemplate.opsForSet().add(adapterKey(userId), token);
        return true;
    }

    @Override
    public boolean put(String userId, Set<String> tokens) {
        stringRedisTemplate.opsForSet().add(adapterKey(userId), tokens.toArray(new String[0]));
        return true;
    }

    @Override
    public synchronized void remove(String userId) {
        stringRedisTemplate.delete(adapterKey(userId));
    }

    @Override
    public boolean contain(String userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(adapterKey(userId)));
    }

    public String adapterKey(String userId) {
        return USER_TOKENS_PREFIX + userId;
    }

}
