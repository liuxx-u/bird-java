package com.bird.websocket.common.synchronizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * web服务拓展同步接口 管理器
 *
 * @author YJ
 */
public class WebSocketServerSyncComposite {

    private List<WebSocketServerSync> syncs = new ArrayList<>();

    @Autowired(required = false)
    public void setSyncs(List<WebSocketServerSync> syncs) {
        if (!CollectionUtils.isEmpty(syncs)) {
            this.syncs = syncs;
        }
    }

    /**
     * 客户端建立连接 时调用
     *
     * @param session session连接
     * @param token   用户token
     */
    public void onOpen(Session session, String token) {
        for (WebSocketServerSync sync : syncs) {
            sync.onOpen(session, token);
        }
    }

    /**
     * 关闭连接时调用
     *
     * @param token 用户token
     */
    public void onClose(String token) {
        for (WebSocketServerSync sync : syncs) {
            sync.onClose(token);
        }
    }

    /**
     * 接收客户端信息
     *
     * @param token   用户token
     * @param message 消息体
     */
    public void onMessage(String token, String message) {
        for (WebSocketServerSync sync : syncs) {
            sync.onMessage(token, message);
        }
    }

    /**
     * 出现错误时调用
     *
     * @param session   session
     * @param throwable 异常信息
     */
    public void onError(Session session, Throwable throwable) {
        for (WebSocketServerSync sync : syncs) {
            sync.onError(session, throwable);
        }
    }
}
