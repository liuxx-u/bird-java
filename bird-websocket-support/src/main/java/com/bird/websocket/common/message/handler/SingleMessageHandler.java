package com.bird.websocket.common.message.handler;

import com.bird.websocket.common.interceptor.MessageInterceptorComposite;
import com.bird.websocket.common.message.MessageSendUtil;
import com.bird.websocket.common.message.SingleMessage;
import com.bird.websocket.common.server.ISessionDirectory;
import com.google.common.collect.Lists;
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

    public SingleMessageHandler(MessageInterceptorComposite messageSyncComposite, ISessionDirectory sessionDirectory) {
        super(messageSyncComposite, sessionDirectory);
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
    protected List<String> getUser(SingleMessage message) {
        List<String> userIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(message.getToken())) {
            String userId = sessionDirectory.getUser(message.getToken());
            if (userId != null) {
                userIds.add(userId);
            }
        }
        if (StringUtils.isNotBlank(message.getUserId())) {
            userIds.add(message.getUserId());
        }
        return userIds;
    }

    @Override
    protected void sendMessage(List<Session> sessions, SingleMessage message) {
        List<Session> successList = Lists.newArrayList();
        List<Session> failList = Lists.newArrayList();
        for (Session session : sessions) {
            if (MessageSendUtil.sendMessage(session, message.getContent())) {
                successList.add(session);
                return;
            }
            failList.add(session);
        }
        messageSyncComposite.sendMessageAfter(message, successList, failList);
    }
}
