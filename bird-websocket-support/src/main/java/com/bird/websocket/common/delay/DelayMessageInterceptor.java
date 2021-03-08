package com.bird.websocket.common.delay;

import com.alibaba.fastjson.JSON;
import com.bird.websocket.common.interceptor.MessageInterceptor;
import com.bird.websocket.common.message.Message;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.util.List;

/**
 * @author YJ
 */
@AllArgsConstructor
public class DelayMessageInterceptor implements MessageInterceptor, Ordered {

    private IDelayMessageStorage delayMessageStorage;
    private long defaultDuration;

    @Override
    public void getSessionAfter(Message message, List<Session> sessions) {
        if (message.isDelay() && CollectionUtils.isEmpty(sessions)) {
            delayCache(message);
        }
    }

    @Override
    public void sendMessageAfter(Message message, List<Session> successSessions, List<Session> failSessions) {
        if (message.isDelay() && CollectionUtils.isEmpty(successSessions)) {
            delayCache(message);
        }
    }

    private void delayCache(Message message) {
        String users = message.getItem(Message.DELAY_USER_KEY);
        if (StringUtils.isBlank(users)) {
            return;
        }
        List<String> userIds = JSON.parseArray(users, String.class);
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        for (String userId : userIds) {
            delayMessageStorage.put(userId, message, getDuration(message));
        }
    }

    private long getDuration(Message message) {
        if (message.getDelayDuration() == 0) {
            return defaultDuration;
        }
        return message.getDelayDuration();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
