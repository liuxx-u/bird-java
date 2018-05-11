package com.bird.core.session;

/**
 * @author liuxx
 * @date 2018/5/11
 *
 * session上下文
 */
public class SessionContext {
    private static final ThreadLocal<BirdSession> LOCAL = ThreadLocal.withInitial(() -> new BirdSession());

    private SessionContext(){}

    /**
     * 获取session信息
     * @return
     */
    public static BirdSession getSession(){
        return LOCAL.get();
    }

    /**
     * 设置session信息
     * @param session
     */
    public static void setSession(BirdSession session){
        LOCAL.set(session);
    }

    /**
     * 移除session信息
     * @param session
     */
    public static void removeSession(BirdSession session){
        LOCAL.remove();
    }
}
