package com.bird.websocket.common.interceptor.log;

import com.bird.websocket.common.interceptor.MessageInterceptor;
import com.bird.websocket.common.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

import javax.websocket.Session;
import java.util.List;

/**
 * @author YJ
 */
@Slf4j
public class LogMessageInterceptor implements MessageInterceptor, Ordered {

    @Override
    public boolean preSendMessage(Message message) {
        if (log.isDebugEnabled()) {
            log.debug("LogMessageInterceptor preSendMessage is called. message = [{}]", message);
        }
        return true;
    }

    @Override
    public void getSessionAfter(Message message, List<Session> sessions) {
        if (log.isDebugEnabled()) {
            log.debug("LogMessageInterceptor getSessionAfter is called. message = [{}], sessions.size = [{}]", message, sessions.size());
        }
    }

    @Override
    public void sendMessageAfter(Message message, List<Session> successSessions, List<Session> failSessions) {
        if (log.isDebugEnabled()) {
            log.debug("LogMessageInterceptor sendMessageAfter is called. message = [{}], " +
                            "successSessions.size = [{}], successSessions.size = [{}]",
                    message, successSessions.size(), failSessions.size());
        }
    }

    @Override
    public void afterCompletion(Message message) {
        if (log.isDebugEnabled()) {
            log.debug("LogMessageInterceptor afterCompletion is called. message = [{}]", message);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
