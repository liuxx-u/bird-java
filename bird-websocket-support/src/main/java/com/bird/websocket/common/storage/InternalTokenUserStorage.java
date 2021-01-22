package com.bird.websocket.common.storage;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Token - 用户 映射 内部存储
 *
 * @author yuanjian
 */
public class InternalTokenUserStorage implements ITokenUserStorage {

    private static final ConcurrentHashMap<String, String> TOKEN_USER_MAP = new ConcurrentHashMap<>();

    @Override
    public String get(String token) {
        return TOKEN_USER_MAP.get(token);
    }

    @Override
    public boolean put(String token, String userId) {
        return StringUtils.equals(TOKEN_USER_MAP.put(token, userId), userId);
    }

    @Override
    public String remove(String token) {
        return TOKEN_USER_MAP.remove(token);
    }

    @Override
    public boolean contain(String userId) {
        return TOKEN_USER_MAP.containsKey(userId);
    }
}
