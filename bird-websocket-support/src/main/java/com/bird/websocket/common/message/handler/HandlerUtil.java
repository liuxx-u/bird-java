package com.bird.websocket.common.message.handler;

import com.bird.websocket.common.message.BroadcastMessage;
import com.bird.websocket.common.message.Message;
import com.bird.websocket.common.message.MultipartMessage;
import com.bird.websocket.common.message.SingleMessage;
import com.bird.websocket.common.server.ISessionDirectory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanjian
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerUtil {

    private static List<IMessageHandler<? extends Message>> handlers;

    private static final List<Class<? extends Message>> ALLOW_MESSAGE_TYPE;
    static {
        ALLOW_MESSAGE_TYPE = new ArrayList<>();
        ALLOW_MESSAGE_TYPE.add(SingleMessage.class);
        ALLOW_MESSAGE_TYPE.add(MultipartMessage.class);
        ALLOW_MESSAGE_TYPE.add(BroadcastMessage.class);
    }

    /**
     * 根据消息数据获取对应的消息处理器
     */
    public static IMessageHandler<? extends Message> getHandler(Message message, ISessionDirectory sessionDirectory) {
        List<IMessageHandler<? extends Message>> handlers = getAllHandlers(sessionDirectory);
        for (IMessageHandler<?> handler : handlers) {
            Class<?> genericType = ReflectionUtils.getSuperInterfaceGenericType(handler.getClass());
            if (genericType.isInstance(message)) {
                return handler;
            }
        }
        return null;
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

    /**
     * 获取所有消息处理器
     */
    public static synchronized List<IMessageHandler<? extends Message>> getAllHandlers(ISessionDirectory sessionDirectory) {
        if (handlers == null) {
            synchronized (HandlerUtil.class) {
                if (handlers == null) {
                    handlers = initHandlers(sessionDirectory);
                }
            }
        }
        return handlers;
    }

    /**
     * 初始化 消息处理器
     */
    private static List<IMessageHandler<? extends Message>> initHandlers(ISessionDirectory sessionDirectory) {
        List<IMessageHandler<? extends Message>> handlers = new ArrayList<>();
        handlers.add(new SingleMessageHandler(sessionDirectory));
        handlers.add(new MultipartMessageHandler(sessionDirectory));
        handlers.add(new BroadcastMessageHandler(sessionDirectory));
        return handlers;
    }
}

/**
 * 反射部分工具类, 独占
 *
 * @author yuanjian
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReflectionUtils {

    /**
     * 通过反射，获取class声明的父接口（IMessageHandler）的泛型参数类型 如：public B implement A<String>
     */
    public static Class<?> getSuperInterfaceGenericType(Class<?> clazz, int index) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface.getTypeName().startsWith(IMessageHandler.class.getName())) {
                Type genType = clazz.getGenericInterfaces()[0];
                if (!(genType instanceof ParameterizedType)) {
                    return Object.class;
                }
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                if (index >= params.length || index < 0) {
                    return Object.class;
                }
                if (!(params[index] instanceof Class)) {
                    return Object.class;
                }
                return (Class<?>) params[index];
            }
        }
        return Object.class;
    }

    public static Class<?> getSuperInterfaceGenericType(Class<?> clazz) {
        return getSuperInterfaceGenericType(clazz, 0);
    }
}