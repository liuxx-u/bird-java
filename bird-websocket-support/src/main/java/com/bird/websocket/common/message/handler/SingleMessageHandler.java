package com.bird.websocket.common.message.handler;

import com.bird.websocket.common.message.MessageSendUtil;
import com.bird.websocket.common.message.SingleMessage;
import com.bird.websocket.common.server.ISessionDirectory;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.Session;
import java.util.LinkedList;
import java.util.List;

/**
 * 单点消息发送处理器
 *
 * @author yuanjian
 */
public class SingleMessageHandler extends AbstractMessageHandler<SingleMessage> {

    public SingleMessageHandler(ISessionDirectory sessionDirectory) {
        super(sessionDirectory);
    }

    @Override
    public List<Session> getSession(SingleMessage message) {
        LinkedList<Session> sessions = new LinkedList<>();
        if (StringUtils.isNotBlank(message.getToken())) {
            Session session = sessionDirectory.getSession(message.getToken());
            if (session != null) {
                sessions.add(session);
            }
        }
        if (StringUtils.isNotBlank(message.getUserId())) {
            List<Session> sessionList = sessionDirectory.getUserSessions(message.getUserId());
            if (sessionList != null) {
                sessions.addAll(sessionList);
            }
        }
        return sessions;
    }

    @Override
    protected void sendMessage(List<Session> sessions, String messageContent) {
        for (Session session : sessions) {
            if (MessageSendUtil.sendMessage(session, messageContent)) {
                return;
            }
        }
    }
}
