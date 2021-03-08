package com.bird.websocket.common.server;

import com.bird.core.SpringContextHolder;
import com.bird.websocket.common.message.MessageSendUtil;
import com.bird.websocket.common.interceptor.WebSocketServerInterceptorComposite;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
    private volatile WebSocketServerInterceptorComposite webSocketServerInterceptorComposite;

    /**
     * 客户端建立连接
     *
     * @param token 用户token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) throws IOException {
        ISessionDirectory directory = this.getSessionDirectory();

        String userId = directory.getUser(token);
        if (StringUtils.isBlank(userId)) {
            // 当前token未登录或已过期
            MessageSendUtil.sendMessage(session, "lose_efficacy");
            session.close();
        }
        if (!directory.add(token, session)) {
            // 当前token已经建立连接
            MessageSendUtil.sendMessage(session, "connection_exists");
            session.close();
        }

        getWebSocketServerInterceptorComposite().onOpen(session, token);
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

        getWebSocketServerInterceptorComposite().onClose(token);
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

        getWebSocketServerInterceptorComposite().onMessage(token, message);
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

        getWebSocketServerInterceptorComposite().onError(session, throwable);
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

    private WebSocketServerInterceptorComposite getWebSocketServerInterceptorComposite() {
        if (this.webSocketServerInterceptorComposite == null) {
            synchronized (WebSocketServer.class) {
                if (this.webSocketServerInterceptorComposite == null) {
                    this.webSocketServerInterceptorComposite = SpringContextHolder.getBean(WebSocketServerInterceptorComposite.class);
                }
            }
        }
        return this.webSocketServerInterceptorComposite;
    }

}
