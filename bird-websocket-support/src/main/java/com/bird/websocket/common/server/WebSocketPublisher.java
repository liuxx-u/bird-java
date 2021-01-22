package com.bird.websocket.common.server;

import com.bird.core.Assert;
import com.bird.websocket.common.message.Message;
import com.bird.websocket.common.message.MessageBuilder;
import com.bird.websocket.common.message.handler.HandlerUtil;
import com.bird.websocket.common.message.handler.IMessageHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liuxx
 * @since 2020/12/30
 */
@Slf4j
@AllArgsConstructor
public class WebSocketPublisher {

    private final ISessionDirectory sessionDirectory;

    /**
     * 发送消息，目前只支持 单点消息、多点消息、广播消息。
     *
     *
     * @param message 消息体, 可以使用 {@link MessageBuilder} 进行消息体的构建
     */
    public void sendMessage(Message message) {
        IMessageHandler handler = HandlerUtil.getHandler(message, sessionDirectory);
        Assert.notNull(handler);
        handler.execute(message);
    }
}

