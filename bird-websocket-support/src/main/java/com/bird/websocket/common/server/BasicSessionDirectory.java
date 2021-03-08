package com.bird.websocket.common.server;

import com.bird.websocket.common.authorize.IAuthorizeResolver;
import com.bird.websocket.common.storage.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * @author yuanjian
 */
public class BasicSessionDirectory implements ISessionDirectory {

    /** 读写锁 */
    private final StampedLock lock = new StampedLock();

    /** user - Tokens */
    private final IUserTokensStorage userTokensStorage;
    /** token - user */
    private final ITokenUserStorage tokenUserStorage;
    /** token - session */
    private final ITokenSessionStorage tokenSessionStorage;
    /** token - userId 解析器 */
    private final IAuthorizeResolver authorizeResolver;

    public BasicSessionDirectory(IAuthorizeResolver authorizeResolver) {
        this.authorizeResolver = authorizeResolver;
        this.userTokensStorage = new InternalUserTokensStorage();
        this.tokenUserStorage = new InternalTokenUserStorage();
        this.tokenSessionStorage = new InternalTokenSessionStorage();
    }

    @Override
    public boolean add(String token, Session session) {
        final long stamp = lock.writeLock();
        try {
            String userId = this.authorizeResolver.resolve(token);
            if (StringUtils.isBlank(userId)) {
                return false;
            }
            Set<String> tokens = userTokensStorage.get(userId);
            if (tokens.add(token)) {
                userTokensStorage.put(userId, tokens);
                tokenUserStorage.put(token, userId);
                tokenSessionStorage.put(token, session);
                return true;
            }
            return false;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public void remove(String token) {
        final long stamp = lock.writeLock();
        try {
            tokenSessionStorage.remove(token);
            String userId = tokenUserStorage.remove(token);
            if (StringUtils.isBlank(userId)) {
                return;
            }

            Set<String> tokens = userTokensStorage.get(userId);
            if (!CollectionUtils.isEmpty(tokens)) {
                tokens.remove(token);
            }
            if (CollectionUtils.isEmpty(tokens)) {
                userTokensStorage.remove(userId);
            }
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public List<Session> getUserSessions(String userId) {
        final long stamp = lock.readLock();
        if (StringUtils.isBlank(userId)) {
            return new ArrayList<>();
        }
        try {
            return userTokensStorage.get(userId)
                    .stream()
                    .map(this::getSession)
                    .collect(Collectors.toList());
        } finally {
            lock.unlockRead(stamp);
        }
    }

    @Override
    public Session getSession(String token) {
        final long stamp = lock.readLock();
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            return tokenSessionStorage.get(token);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    @Override
    public String getUser(String token) {
        final long stamp = lock.readLock();
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            return this.authorizeResolver.resolve(token);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    @Override
    public List<String> getAllUser() {
        final long stamp = lock.readLock();
        try {
            List<String> allToken = this.tokenSessionStorage.getAllToken();
            if (allToken != null) {
                return allToken.stream().map(authorizeResolver::resolve)
                        .collect(Collectors.toList());
            }
            return Lists.newArrayList();
        } finally {
            lock.unlockRead(stamp);
        }
    }

    @Override
    public List<Session> getAllSession() {
        final long stamp = lock.readLock();
        try {
            return new ArrayList<>(tokenSessionStorage.getAll());
        } finally {
            lock.unlockRead(stamp);
        }
    }
}
