package com.bird.websocket.common.server;

import com.bird.core.SpringContextHolder;
import com.bird.websocket.common.authorize.IAuthorizeResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxx
 * @since 2020/12/29
 */
@Slf4j
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    /**
     * userId与websocket客户端Session的映射表
     */
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();


    /**
     * 客户端建立连接
     *
     * @param token 用户token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) {
        String userId = this.resolveUserId(token);
        if (StringUtils.isBlank(userId)) {
            this.sendMessage(session, "当前用户未登录");
            return;
        }
        sessionPools.put(token, session);
    }

    /**
     * 关闭连接时调用
     *
     * @param token 用户token
     */
    @OnClose
    public void onClose(@PathParam(value = "token") String token) {
        String userId = this.resolveUserId(token);

        if (StringUtils.isNotBlank(userId)) {
            sessionPools.remove(token);
        }
    }

    /**
     * 接收客户端信息
     *
     * @param token   用户token
     * @param message 消息体
     */
    @OnMessage
    public void onMessage(@PathParam(value = "token") String token, String message) {
        //TODO: 暂不处理
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
        Arrays.stream(userIds).forEach(userId -> this.sendMessage(sessionPools.get(userId), message));
    }

    /**
     * 向客户端发送消息
     *
     * @param session 客户端session
     * @param message 消息体
     */
    private void sendMessage(Session session, String message) {
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                log.error("ws消息发送失败:{}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * 通过token解析userId
     * @param token token
     * @return userId
     */
    private String resolveUserId(String token){
        if(StringUtils.isBlank(token)){
            return token;
        }
        IAuthorizeResolver authorizeResolver = SpringContextHolder.getBean(IAuthorizeResolver.class);
        return authorizeResolver.resolve(token);
    }
}
