package com.bird.websocket.common.interceptor;

import com.bird.websocket.common.message.Message;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YJ
 */
public class MessageInterceptorComposite {

    private List<MessageInterceptor> messageInterceptors = new ArrayList<>();

    public MessageInterceptorComposite(List<MessageInterceptor> messageInterceptors) {
        if (!CollectionUtils.isEmpty(messageInterceptors)) {
            this.messageInterceptors = messageInterceptors;
        }
    }

    /**
     * 发送消息前
     *
     * @param message 消息体
     * @return 是否继续发送
     */
    public boolean preSendMessage(Message message) {
        for (MessageInterceptor messageInterceptor : messageInterceptors) {
            if (!messageInterceptor.preSendMessage(message)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取到的所有session后
     *
     * @param message  消息体
     * @param sessions session集合
     */
    public void getSessionAfter(Message message, List<Session> sessions) {
        for (MessageInterceptor messageInterceptor : messageInterceptors) {
            messageInterceptor.getSessionAfter(message, sessions);
        }
    }

    /**
     * 发送消息之后
     *
     * @param message         消息体
     * @param successSessions 成功发送的session
     * @param failSessions    发送失败的session
     */
    public void sendMessageAfter(Message message, List<Session> successSessions, List<Session> failSessions) {
        for (MessageInterceptor messageInterceptor : messageInterceptors) {
            messageInterceptor.sendMessageAfter(message, successSessions, failSessions);
        }
    }

    /**
     * 消息完成之后
     *
     * @param message 消息体
     */
    public void afterCompletion(Message message) {
        for (int i = messageInterceptors.size(); i > 0; i--) {
            MessageInterceptor messageInterceptor = messageInterceptors.get(i - 1);
            messageInterceptor.afterCompletion(message);
        }
    }
}
