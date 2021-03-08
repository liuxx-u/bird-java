package com.bird.websocket.common.message.handler;

import com.bird.websocket.common.interceptor.MessageInterceptorComposite;
import com.bird.websocket.common.message.*;
import com.bird.websocket.common.server.ISessionDirectory;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yuanjian
 */
public class MessageHandlerFactory {

    private Map<MessageTypeEnum, IMessageHandler<? extends Message>> handlers;

    public MessageHandlerFactory(MessageInterceptorComposite messageSyncComposite, ISessionDirectory sessionDirectory) {
        this.handlers = initHandlerMap(messageSyncComposite, sessionDirectory);
    }

    /**
     * 根据消息数据获取对应的消息处理器
     */
    public IMessageHandler<? extends Message> getHandler(Message message) {
        AllowMessageRecognize.validateMessageType(message);
        for (Map.Entry<MessageTypeEnum, IMessageHandler<? extends Message>> handlerEntry : handlers.entrySet()) {
            if (handlerEntry.getKey().equals(message.getType())) {
                return handlerEntry.getValue();
            }
        }
        return null;
    }

    public Map<MessageTypeEnum, IMessageHandler<? extends Message>> initHandlerMap(MessageInterceptorComposite messageSyncComposite, ISessionDirectory sessionDirectory) {
        Map<MessageTypeEnum, IMessageHandler<? extends Message>> handlerMap = Maps.newHashMap();
        handlerMap.put(MessageTypeEnum.SINGLE_POINT, new SingleMessageHandler(messageSyncComposite, sessionDirectory));
        handlerMap.put(MessageTypeEnum.MULTIPART, new MultipartMessageHandler(messageSyncComposite, sessionDirectory));
        handlerMap.put(MessageTypeEnum.BROADCAST, new BroadcastMessageHandler(messageSyncComposite, sessionDirectory));
        return handlerMap;
    }

    /**
     * 允许的消息类型
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class AllowMessageRecognize {

        private static final List<Class<? extends Message>> ALLOW_MESSAGE_TYPE;

        static {
            ALLOW_MESSAGE_TYPE = new ArrayList<>();
            ALLOW_MESSAGE_TYPE.add(SingleMessage.class);
            ALLOW_MESSAGE_TYPE.add(MultipartMessage.class);
            ALLOW_MESSAGE_TYPE.add(BroadcastMessage.class);
        }

        /**
         * 验证允许的消息类型
         */
        public static void validateMessageType(Message message) {
            for (Class<? extends Message> messageClazz : ALLOW_MESSAGE_TYPE) {
                if (messageClazz.isInstance(message)) {
                    return;
                }
            }
            throw new IllegalArgumentException("Message parameter types only allow SingleMessage, MultipartMessage, BroadcastMessage");
        }
    }
}