package com.bird.websocket.common.synchronizer;

import com.bird.websocket.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YJ
 */
public class MessageSyncComposite {

    private List<MessageSync> messageSyncs = new ArrayList<>();

    @Autowired(required = false)
    public void setMessageSyncs(List<MessageSync> messageSyncs) {
        if (!CollectionUtils.isEmpty(messageSyncs)) {
            this.messageSyncs = messageSyncs;
        }
    }

    /**
     * 发送消息前
     *
     * @param message 消息体
     * @return 是否继续发送
     */
    public boolean preSendMessage(Message message) {
        for (MessageSync messageSync : messageSyncs) {
            if (!messageSync.preSendMessage(message)) {
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
        for (MessageSync messageSync : messageSyncs) {
            messageSync.getSessionAfter(message, sessions);
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
        for (MessageSync messageSync : messageSyncs) {
            messageSync.sendMessageAfter(message, successSessions, failSessions);
        }
    }

    /**
     * 消息完成之后
     *
     * @param message 消息体
     */
    public void afterCompletion(Message message) {
        for (MessageSync messageSync : messageSyncs) {
            messageSync.afterCompletion(message);
        }
    }
}
