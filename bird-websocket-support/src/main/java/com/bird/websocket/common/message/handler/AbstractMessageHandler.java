package com.bird.websocket.common.message.handler;

import cn.hutool.core.thread.ThreadUtil;
import com.bird.websocket.common.message.BasicMessage;
import com.bird.websocket.common.server.ISessionDirectory;
import com.bird.websocket.common.synchronizer.MessageSyncComposite;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author yuanjian
 */
@Slf4j
@AllArgsConstructor
public abstract class AbstractMessageHandler<T extends BasicMessage> implements IMessageHandler<T> {

    protected final MessageSyncComposite messageSyncComposite;
    protected final ISessionDirectory sessionDirectory;

    @Override
    public void execute(T message) {
        if (!messageSyncComposite.preSendMessage(message)) {
            log.info("Message Delivery Cancellation, {}", message);
            return;
        }

        List<Session> sessions = this.getSession(message);
        messageSyncComposite.getSessionAfter(message, sessions);

        if (CollectionUtils.isEmpty(sessions)) {
            return;
        }
        sessions = new ArrayList<>(new HashSet<>(sessions));
        if (message.isAsync()) {
            List<Session> finalSessions = sessions;
            ThreadUtil.execute(() -> this.sendMessage(finalSessions, message));
            return;
        }
        this.sendMessage(sessions, message);

        messageSyncComposite.afterCompletion(message);
    }

    protected void sessionSuccess(Session session) {

    }

    /**
     * 获取待发送消息的session
     *
     * @param message 消息参数
     * @return 待发送的session信息
     */
    protected abstract List<Session> getSession(T message);

    /**
     * 发送消息
     *
     * @param sessions 待发送的session信息
     * @param message  消息参数
     */
    protected abstract void sendMessage(List<Session> sessions, T message);

}
