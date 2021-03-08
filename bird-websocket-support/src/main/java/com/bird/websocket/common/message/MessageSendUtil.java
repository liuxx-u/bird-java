package com.bird.websocket.common.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author yuanjian
 */
@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSendUtil {

    private final Session session;

    public boolean send(String messageContent) {
        // 增加同步，防止多个线程同时往同一个session写数据
        synchronized (session) {
            if (!session.isOpen()) {
                log.warn("[webSocket sendMessage] session is not open]");
                return false;
            }

            RemoteEndpoint.Basic basic = session.getBasicRemote();
            if (basic == null) {
                log.warn("[webSocket sendMessage] session RemoteEndpoint is null]");
                return false;
            }
            try {
                session.getBasicRemote().sendText(messageContent);
            } catch (IOException e) {
                log.warn(MessageFormat.format("[webSocket sendMessage] session({}) 发送消息({}) send error", session, messageContent), e);
                return false;
            }
            return true;
        }
    }

    /**
     * 发送消息
     *
     * @param session        Session
     * @param messageContent 消息
     */
    public static boolean sendMessage(Session session, String messageContent) {
        if (session == null) {
            log.warn("[webSocket sendMessage] session is null]");
            return false;
        }
        return new MessageSendUtil(session).send(messageContent);
    }
}
