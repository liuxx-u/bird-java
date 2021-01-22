package com.bird.websocket.common.storage;

import com.bird.websocket.common.ITokenSessionStorage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Token - Session 映射 Redis存储
 *
 * @author yuanjian
 */
public class RedisTokenSessionStorage implements ITokenSessionStorage {

    private static final String TOKEN_SESSION_PREFIX = "bird.token_session.";
    private static final String OBJECT_KEY = "Session";

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisTokenSessionStorage(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Session get(String token) {
        Object session = redisTemplate.opsForHash().get(adapterKey(token), OBJECT_KEY);
        return session != null ? (Session) session : null;
    }

    @Override
    public boolean put(String token, Session session) {
        redisTemplate.opsForHash().put(adapterKey(token), OBJECT_KEY, session);
        return true;
    }

    @Override
    public void remove(String token) {
        redisTemplate.delete(adapterKey(token));
    }

    @Override
    public boolean contain(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(adapterKey(token)));
    }

    @Override
    public List<Session> getAll() {
        List<Session> sessions = new ArrayList<>();
        Set<Object> keys = redisTemplate.keys(adapterKey("*"));
        if (CollectionUtils.isNotEmpty(keys)) {
            for (Object key : keys) {
                String tokenKey = String.valueOf(key);

                Session session = this.get(tokenKey);
                if (session != null) {
                    sessions.add(session);
                }
            }
        }
        return sessions;
    }

    public String adapterKey(String token) {
        if (StringUtils.startsWith(token, TOKEN_SESSION_PREFIX)) {
            return token;
        }
        return TOKEN_SESSION_PREFIX + token;
    }
}
