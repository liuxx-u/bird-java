package com.bird.websocket.common.message.handler;

import com.bird.websocket.common.message.BroadcastMessage;
import com.bird.websocket.common.message.MessageSendUtil;
import com.bird.websocket.common.server.ISessionDirectory;
import com.bird.websocket.common.synchronizer.MessageSyncComposite;
import com.google.common.collect.Lists;

import javax.websocket.Session;
import java.util.List;

/**
 * @author yuanjian
 */
public class BroadcastMessageHandler extends AbstractMessageHandler<BroadcastMessage> {

    public BroadcastMessageHandler(MessageSyncComposite messageSyncComposite, ISessionDirectory sessionDirectory) {
        super(messageSyncComposite, sessionDirectory);
    }

    @Override
    protected List<Session> getSession(BroadcastMessage message) {
        return sessionDirectory.getAllSession();
    }

    @Override
    protected void sendMessage(List<Session> sessions, BroadcastMessage message) {
        List<Session> successList = Lists.newArrayList();
        List<Session> failList = Lists.newArrayList();

        for (Session session : sessions) {
            if (MessageSendUtil.sendMessage(session, message.getContent())) {
                successList.add(session);
                continue;
            }
            failList.add(session);
        }
        messageSyncComposite.sendMessageAfter(message, successList, failList);
    }
}
