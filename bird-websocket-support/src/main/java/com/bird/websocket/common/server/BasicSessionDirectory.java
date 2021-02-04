package com.bird.websocket.common.server;

import com.bird.websocket.common.authorize.IAuthorizeResolver;
import com.bird.websocket.common.storage.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yuanjian
 */
public class BasicSessionDirectory implements ISessionDirectory {

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
    }

    @Override
    public void remove(String token) {
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
    }

    @Override
    public List<Session> getUserSessions(String userId) {
        if (StringUtils.isBlank(userId)) {
            return new ArrayList<>();
        }
        return userTokensStorage.get(userId)
                .stream()
                .map(this::getSession)
                .collect(Collectors.toList());
    }

    @Override
    public Session getSession(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return tokenSessionStorage.get(token);
    }

    @Override
    public List<Session> getAllSession() {
        return new ArrayList<>(tokenSessionStorage.getAll());
    }
}
