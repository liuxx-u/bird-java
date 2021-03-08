package com.bird.websocket.common.interceptor;

import javax.websocket.Session;

/**
 * web服务拓展同步接口
 *
 * @author YJ
 */
public interface WebSocketServerInterceptor {

    /**
     * 客户端建立连接 时调用
     *
     * @param session session连接
     * @param token   用户token
     */
    default void onOpen(Session session, String token) {
    }

    /**
     * 关闭连接时调用
     *
     * @param token 用户token
     */
    default void onClose(String token) {
    }

    /**
     * 接收客户端信息
     *
     * @param token   用户token
     * @param message 消息体
     */
    default void onMessage(String token, String message) {
    }

    /**
     * 出现错误时调用
     *
     * @param session   session
     * @param throwable 异常信息
     */
    default void onError(Session session, Throwable throwable) {
    }
}
