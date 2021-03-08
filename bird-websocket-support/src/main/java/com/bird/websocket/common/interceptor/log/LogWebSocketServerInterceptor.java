package com.bird.websocket.common.interceptor.log;

import com.bird.websocket.common.interceptor.WebSocketServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

import javax.websocket.Session;

/**
 * @author YJ
 */
@Slf4j
public class LogWebSocketServerInterceptor implements WebSocketServerInterceptor, Ordered {

    @Override
    public void onOpen(Session session, String token) {
        if (log.isDebugEnabled()) {
            log.debug("LogWebSocketServerInterceptor onOpen is called. token = [{}]", token);
        }
    }

    @Override
    public void onClose(String token) {
        if (log.isDebugEnabled()) {
            log.debug("LogWebSocketServerInterceptor onClose is called. token = [{}]", token);
        }
    }

    @Override
    public void onMessage(String token, String message) {
        if (log.isDebugEnabled()) {
            log.debug("LogWebSocketServerInterceptor onMessage is called. token = [{}], message = [{}]", token, message);
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        if (log.isDebugEnabled()) {
            log.debug("LogWebSocketServerInterceptor onError is called. token = [{}]", throwable.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
