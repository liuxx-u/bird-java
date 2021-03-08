package com.bird.websocket.common.interceptor;

import com.bird.websocket.common.message.Message;

import javax.websocket.Session;
import java.util.List;

/**
 * 消息发送同步器
 *
 * @author YJ
 */
public interface MessageInterceptor {

    /**
     * 发送消息前
     *
     * @param message 消息体
     * @return 是否继续发送
     */
    default boolean preSendMessage(Message message) {
        return true;
    }

    /**
     * 获取到的所有session后
     *
     * @param message  消息体
     * @param sessions session集合
     */
    default void getSessionAfter(Message message, List<Session> sessions) {
    }

    /**
     * 发送消息之后
     *
     * @param message         消息体
     * @param successSessions 成功发送的session
     * @param failSessions    发送失败的session
     */
    default void sendMessageAfter(Message message, List<Session> successSessions, List<Session> failSessions) {
    }

    /**
     * 消息完成之后
     *
     * @param message 消息体
     */
    default void afterCompletion(Message message) {
    }
}
