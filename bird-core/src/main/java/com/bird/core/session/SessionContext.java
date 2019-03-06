package com.bird.core.session;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/5/11
 *
 * session上下文
 */
public class SessionContext {
    private static final ThreadLocal<BirdSession> LOCAL = ThreadLocal.withInitial(BirdSession::new);

    private SessionContext() {
    }

    /**
     * 获取session信息
     *
     * @return session
     */
    public static BirdSession getSession() {
        return LOCAL.get();
    }

    /**
     * 获取当前登录用户的userId
     *
     * @return userId
     */
    public static Serializable getUid() {
        BirdSession session = getSession();
        if (session == null) return null;
        return session.getUserId();
    }

    /**
     * 获取当前登录用户userId
     *
     * @return userId
     */
    public static Long getUserId() {
        Serializable uid = getUid();
        if (uid == null) return 0L;
        if (StringUtils.isBlank(uid.toString())) return 0L;
        return Long.valueOf(uid.toString());
    }

    /**
     * 设置session信息
     *
     * @param session session
     */
    public static void setSession(BirdSession session) {
        LOCAL.set(session);
    }

    /**
     * 移除session信息
     */
    public static void removeSession() {
        LOCAL.remove();
    }
}
