package com.bird.web.sso.server.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * JVM缓存实现的Client Store
 *
 * @author liuxx
 * @date 2019/3/29
 */
public class CacheClientStore implements IClientStore {

    private Cache<String, Set<String>> cache;

    public CacheClientStore(Integer expire) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(expire, TimeUnit.MINUTES).build();
    }

    @Override
    public void store(String token, String clientHost) {
        if (StringUtils.isAnyBlank(token, clientHost)) {
            return;
        }

        Set<String> clients = cache.getIfPresent(token);
        if (clients == null) {
            clients = new LinkedHashSet<>();
        }
        Set<String> clientHosts = Arrays.stream(StringUtils.split(clientHost, ","))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());

        clients.addAll(clientHosts);
        cache.put(token, clients);
    }

    @Override
    public Set<String> getAll(String token) {
        return cache.getIfPresent(token);
    }

    @Override
    public void remove(String token) {
        cache.invalidate(token);
    }
}
