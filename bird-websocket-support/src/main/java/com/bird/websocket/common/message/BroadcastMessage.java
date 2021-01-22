package com.bird.websocket.common.message;

/**
 * 广播消息
 *
 * @author yuanjian
 */
public class BroadcastMessage extends BasicMessage {

    @Override
    public MessageTypeEnum getType() {
        return MessageTypeEnum.BROADCAST;
    }
}
