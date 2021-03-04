package com.bird.websocket.common.server;

import com.bird.core.SpringContextHolder;
import com.bird.websocket.common.message.MessageSendUtil;
import com.bird.websocket.common.synchronizer.WebSocketServerSyncComposite;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author liuxx
 * @since 2020/12/29
 */
@Slf4j
@NoArgsConstructor
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    private volatile ISessionDirectory sessionDirectory;
    private volatile WebSocketServerSyncComposite webSocketServerSyncComposite;

    /**
     * 客户端建立连接
     *
     * @param token 用户token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) throws IOException {
        ISessionDirectory directory = this.getSessionDirectory();
        if (!directory.add(token, session)) {
            // 当前token已经建立连接
            MessageSendUtil.sendMessage(session, "用户未登录或当前token已经建立连接");
            session.close();
        }

        getWebSocketServerSyncComposite().onOpen(session, token);
    }

    /**
     * 关闭连接时调用
     *
     * @param token 用户token
     */
    @OnClose
    public void onClose(@PathParam(value = "token") String token) {
        ISessionDirectory directory = this.getSessionDirectory();
        directory.remove(token);

        getWebSocketServerSyncComposite().onClose(token);
    }

    /**
     * 接收客户端信息
     *
     * @param token   用户token
     * @param message 消息体
     */
    @OnMessage
    public void onMessage(@PathParam(value = "token") String token, String message) {
        ISessionDirectory directory = this.getSessionDirectory();
        Session session = directory.getSession(token);
        MessageSendUtil.sendMessage(session, message);

        getWebSocketServerSyncComposite().onMessage(token, message);
    }

    /**
     * 出现错误时调用
     *
     * @param session   session
     * @param throwable 异常信息
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error(throwable.getMessage(), throwable);

        getWebSocketServerSyncComposite().onError(session, throwable);
    }

    private ISessionDirectory getSessionDirectory() {
        if (this.sessionDirectory == null) {
            synchronized (WebSocketServer.class) {
                if (this.sessionDirectory == null) {
                    this.sessionDirectory = SpringContextHolder.getBean(ISessionDirectory.class);
                }
            }
        }
        return this.sessionDirectory;
    }

    private WebSocketServerSyncComposite getWebSocketServerSyncComposite() {
        if (this.webSocketServerSyncComposite == null) {
            synchronized (WebSocketServer.class) {
                if (this.webSocketServerSyncComposite == null) {
                    this.webSocketServerSyncComposite = SpringContextHolder.getBean(WebSocketServerSyncComposite.class);
                }
            }
        }
        return this.webSocketServerSyncComposite;
    }

}
