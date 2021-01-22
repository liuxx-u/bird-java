package com.bird.websocket.common.storage;

import com.bird.websocket.common.IUserTokensStorage;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 用户 - Tokens 映射 内部存储
 *
 * @author yuanjian
 */
public class InternalUserTokensStorage implements IUserTokensStorage {

    private static final ConcurrentHashMap<String, Set<String>> USER_TOKEN_MAP = new ConcurrentHashMap<>();

    @Override
    public Set<String> get(String userId) {
        return USER_TOKEN_MAP.getOrDefault(userId, new CopyOnWriteArraySet<>());
    }

    @Override
    public boolean add(String userId, String token) {
        Set<String> tokens = this.get(userId);
        return tokens.add(token);
    }

    @Override
    public boolean put(String userId, Set<String> tokens) {
        if (tokens == null) {
            tokens = new CopyOnWriteArraySet<>();
        }
        return USER_TOKEN_MAP.put(userId, tokens) != null;
    }

    @Override
    public void remove(String userId) {
        USER_TOKEN_MAP.remove(userId);
    }

    @Override
    public boolean contain(String userId) {
        return USER_TOKEN_MAP.containsKey(userId);
    }
}
