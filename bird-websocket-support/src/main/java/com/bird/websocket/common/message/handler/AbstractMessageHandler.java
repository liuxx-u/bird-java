package com.bird.websocket.common.message.handler;

import cn.hutool.core.thread.ThreadUtil;
import com.bird.websocket.common.message.BasicMessage;
import com.bird.websocket.common.server.ISessionDirectory;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author yuanjian
 */
@AllArgsConstructor
public abstract class AbstractMessageHandler<T extends BasicMessage> implements IMessageHandler<T> {

    protected final ISessionDirectory sessionDirectory;

    @Override
    public void execute(T message) {
        List<Session> sessions = this.getSession(message);
        if (CollectionUtils.isEmpty(sessions)) {
            return;
        }
        sessions = new ArrayList<>(new HashSet<>(sessions));
        if (message.isAsync()) {
            List<Session> finalSessions = sessions;
            ThreadUtil.execute(() -> this.sendMessage(finalSessions, message.getContent()));
            return;
        }
        this.sendMessage(sessions, message.getContent());
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
     * @param sessions       待发送的session信息
     * @param messageContent 消息体内容
     */
    protected abstract void sendMessage(List<Session> sessions, String messageContent);
}
