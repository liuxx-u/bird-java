package com.bird.websocket.common.message.handler;

import com.bird.websocket.common.message.BroadcastMessage;
import com.bird.websocket.common.message.MessageSendUtil;
import com.bird.websocket.common.server.ISessionDirectory;

import javax.websocket.Session;
import java.util.List;

/**
 * @author yuanjian
 */
public class BroadcastMessageHandler extends AbstractMessageHandler<BroadcastMessage> {

    public BroadcastMessageHandler(ISessionDirectory sessionDirectory) {
        super(sessionDirectory);
    }

    @Override
    protected List<Session> getSession(BroadcastMessage message) {
        return sessionDirectory.getAllSession();
    }

    @Override
    protected void sendMessage(List<Session> sessions, String messageContent) {
        for (Session session : sessions) {
            MessageSendUtil.sendMessage(session, messageContent);
        }
    }
}
