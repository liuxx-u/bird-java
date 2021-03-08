package com.bird.websocket.common.server;

import com.bird.websocket.common.authorize.IAuthorizeResolver;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 默认的字典服务，存储token - userId - session的映射关系
 *
 * @author liuxx
 * @since 2020/12/29
 */
public class DefaultSessionDirectory implements ISessionDirectory {

    /**
     * userId - Set<token> 映射关系
     */
    private static ConcurrentHashMap<String, Set<String>> USER_TOKEN_MAP = new ConcurrentHashMap<>();
    /**
     * token - userId 映射关系
     */
    private static ConcurrentHashMap<String, String> TOKEN_USERID_MAP = new ConcurrentHashMap<>();
    /**
     * token - session 映射关系
     */
    private static ConcurrentHashMap<String, Session> TOKEN_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * token - userId 解析器
     */
    private final IAuthorizeResolver authorizeResolver;

    public DefaultSessionDirectory(IAuthorizeResolver authorizeResolver) {
        this.authorizeResolver = authorizeResolver;
    }


    /**
     * 添加token
     *
     * @param token token
     */
    @Override
    public boolean add(String token, Session session) {
        String userId = this.authorizeResolver.resolve(token);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        Set<String> tokens = USER_TOKEN_MAP.getOrDefault(userId, new CopyOnWriteArraySet<>());
        if (tokens.add(token)) {
            USER_TOKEN_MAP.put(userId, tokens);
            TOKEN_USERID_MAP.put(token, userId);
            TOKEN_SESSION_MAP.put(token, session);
            return true;
        }
        return false;
    }

    /**
     * 移除token
     *
     * @param token token
     */
    @Override
    public void remove(String token) {
        TOKEN_SESSION_MAP.remove(token);
        String userId = TOKEN_USERID_MAP.remove(token);
        if (StringUtils.isBlank(userId)) {
            return;
        }

        Set<String> tokens = USER_TOKEN_MAP.get(userId);
        if (!CollectionUtils.isEmpty(tokens)) {
            tokens.remove(token);
        }
        if (CollectionUtils.isEmpty(tokens)) {
            USER_TOKEN_MAP.remove(userId);
        }
    }

    /**
     * 根据userId获取所有socket连接
     *
     * @param userId userId
     * @return session集合
     */
    @Override
    public List<Session> getUserSessions(String userId) {
        if (StringUtils.isBlank(userId)) {
            return new ArrayList<>();
        }

        return USER_TOKEN_MAP.getOrDefault(userId, new CopyOnWriteArraySet<>())
                .stream()
                .map(this::getSession)
                .collect(Collectors.toList());
    }

    /**
     * 根据token获取session
     *
     * @param token token
     * @return session
     */
    @Override
    public Session getSession(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return TOKEN_SESSION_MAP.get(token);
    }

    @Override
    public String getUser(String token) {
        return this.authorizeResolver.resolve(token);
    }

    @Override
    public List<String> getAllUser() {
        List<String> allToken = Lists.newArrayList(TOKEN_SESSION_MAP.keySet());
        return allToken.stream().map(authorizeResolver::resolve)
                .collect(Collectors.toList());
    }

    @Override
    public List<Session> getAllSession() {
        return new ArrayList<>(TOKEN_SESSION_MAP.values());
    }
}
