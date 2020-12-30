package com.bird.websocket.common.server;

import com.bird.core.session.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @since 2020/12/30
 */
@Slf4j
public class WebSocketPublisher {

    private final WebSocketServer socketServer;
    private final ISessionDirectory sessionDirectory;

    public WebSocketPublisher(WebSocketServer socketServer, ISessionDirectory sessionDirectory) {
        this.socketServer = socketServer;
        this.sessionDirectory = sessionDirectory;
    }

    /**
     * 向当前登录用户发送消息
     * @param message 消息体
     */
    public void sendMessage(String message){
        this.sendMessage(message, SessionContext.getUserId());
    }

    /**
     * 向指定用户发送消息
     *
     * @param message 消息体
     * @param userIds 接收消息的userId集合
     */
    public void sendMessage(String message, String... userIds) {
        if (StringUtils.isBlank(message) || ArrayUtils.isEmpty(userIds)) {
            log.warn("消息体或接收的用户为空，消息发送取消");
        }
        for (String userId : userIds) {
            this.sessionDirectory.getUserSessions(userId)
                    .forEach(session -> socketServer.sendMessage(session, message));
        }
    }
}
