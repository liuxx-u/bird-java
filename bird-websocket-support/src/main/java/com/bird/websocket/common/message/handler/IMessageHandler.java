package com.bird.websocket.common.message.handler;

import com.bird.websocket.common.message.Message;

/**
 * 消息处理器
 *
 * @author yuanjian
 */
public interface IMessageHandler<T extends Message> {

    /**
     * 执行处理消息
     *
     * @param message 消息
     */
    void execute(T message);
}
