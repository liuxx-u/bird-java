package com.bird.websocket.common.storage;

import com.google.common.collect.Lists;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token - Session 映射 内部存储
 *
 * @author yuanjian
 */
public class InternalTokenSessionStorage implements ITokenSessionStorage {

    private static final ConcurrentHashMap<String, Session> TOKEN_SESSION_MAP = new ConcurrentHashMap<>();

    @Override
    public Session get(String token) {
        return TOKEN_SESSION_MAP.get(token);
    }

    @Override
    public boolean put(String token, Session session) {
        return TOKEN_SESSION_MAP.put(token, session) == session;
    }

    @Override
    public void remove(String token) {
        TOKEN_SESSION_MAP.remove(token);
    }

    @Override
    public boolean contain(String token) {
        return TOKEN_SESSION_MAP.containsKey(token);
    }

    @Override
    public List<String> getAllToken() {
        return Lists.newArrayList(TOKEN_SESSION_MAP.keySet());
    }

    @Override
    public List<Session> getAll() {
        return new ArrayList<>(TOKEN_SESSION_MAP.values());
    }
}
