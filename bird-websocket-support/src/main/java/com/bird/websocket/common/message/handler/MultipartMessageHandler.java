package com.bird.websocket.common.message.handler;

import com.bird.websocket.common.message.MessageSendUtil;
import com.bird.websocket.common.message.MultipartMessage;
import com.bird.websocket.common.server.ISessionDirectory;
import org.apache.commons.collections.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量消息发送处理器
 *
 * @author yuanjian
 */
public class MultipartMessageHandler extends AbstractMessageHandler<MultipartMessage> {

    public MultipartMessageHandler(ISessionDirectory sessionDirectory) {
        super(sessionDirectory);
    }

    @Override
    protected List<Session> getSession(MultipartMessage message) {
        List<Session> sessions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(message.getTokens())) {
            for (String token : message.getTokens()) {
                Session session = sessionDirectory.getSession(token);
                if (session != null) {
                    sessions.add(session);
                }
            }
        }
        if (CollectionUtils.isEmpty(sessions) && CollectionUtils.isNotEmpty(message.getUserIds())) {
            for (String userId : message.getUserIds()) {
                List<Session> sessionList = sessionDirectory.getUserSessions(userId);
                if (sessionList != null) {
                    sessions.addAll(sessionList);
                }
            }
        }
        return sessions;
    }

    @Override
    protected void sendMessage(List<Session> sessions, String messageContent) {
        for (Session session : sessions) {
            MessageSendUtil.sendMessage(session, messageContent);
        }
    }
}
