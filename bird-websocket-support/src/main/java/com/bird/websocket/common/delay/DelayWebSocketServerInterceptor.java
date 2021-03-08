package com.bird.websocket.common.delay;

import com.bird.websocket.common.interceptor.WebSocketServerInterceptor;
import com.bird.websocket.common.message.Message;
import com.bird.websocket.common.message.MessageSendUtil;
import com.bird.websocket.common.server.ISessionDirectory;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;

import javax.websocket.Session;

/**
 * @author YJ
 */
@AllArgsConstructor
public class DelayWebSocketServerInterceptor implements WebSocketServerInterceptor, Ordered {

    private IDelayMessageStorage delayMessageStorage;
    private ISessionDirectory sessionDirectory;

    @Override
    public void onOpen(Session session, String token) {
        if (!session.isOpen()) {
            return;
        }
        String userId = sessionDirectory.getUser(token);
        Message message;
        while ((message = delayMessageStorage.pop(userId)) != null) {
            MessageSendUtil.sendMessage(session, message.getContent());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
