package com.bird.web.sso.server.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JVM缓存实现的Client Store
 *
 * @author liuxx
 * @date 2019/3/29
 */
public class CacheClientStore implements IClientStore {

    private Cache<String, List<String>> cache;

    public CacheClientStore(Integer expire) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(expire, TimeUnit.MINUTES).build();
    }

    @Override
    public void store(String token, String clientHost) {
        List<String> clients = cache.getIfPresent(token);
        if (clients == null) {
            clients = new ArrayList<>();
        }
        if (!clients.contains(clientHost)) {
            clients.add(clientHost);
            cache.put(token, clients);
        }
    }

    @Override
    public List<String> getAll(String token) {
        return cache.getIfPresent(token);
    }

    @Override
    public void remove(String token) {
        cache.invalidate(token);
    }
}
