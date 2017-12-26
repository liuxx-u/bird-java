package com.bird.core.event.handler;

import com.bird.core.event.arg.IEventArg;
import com.bird.core.utils.ClassHelper;
import com.bird.core.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
public class DefaultEventHandlerFactory implements IEventHandlerFactory {

    private static Map<String, Set<Class<?>>> eventHandlerContainer = new HashMap<>();

    /**
     * 初始化事件处理容器
     */
    @PostConstruct
    public static void init() {
        Set<Class<?>> classes = ClassHelper.getClasses("com.bird");
        if (classes != null) {
            String handlerClassName = (IEventHandler.class).getName();
            Set<Class<?>> handlers = ClassHelper.getByInterface(IEventHandler.class, classes);
            for (Class<?> handler : handlers) {
                for (Type baseType : handler.getGenericInterfaces()) {
                    if (!StringUtils.startsWith(baseType.getTypeName(), handlerClassName)) continue;

                    ParameterizedType p = (ParameterizedType) baseType;
                    Class argClass = (Class) p.getActualTypeArguments()[0];
                    String argClassName = argClass.getName();

                    if (!eventHandlerContainer.containsKey(argClassName)) {
                        eventHandlerContainer.put(argClassName, new LinkedHashSet<>());
                    }
                    Set eventHandlers = eventHandlerContainer.get(argClassName);
                    eventHandlers.add(handler);
                    eventHandlerContainer.put(argClassName, eventHandlers);
                }
            }
        }
    }

    /**
     * 获取当前程序所有事件的名称
     * @return
     */
    public static String[] getAllTopics() {
        Set<String> keys = eventHandlerContainer.keySet();
        //如果开启EventBus，且未处理任何的事件，返回默认topics;
        if (keys.size() == 0) {
            return new String[]{"none-topic"};
        }
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * 获取事件处理器
     *
     * @param eventArg
     * @return
     */
    @Override
    public Set<IEventHandler> getHandlers(IEventArg eventArg) {
        String eventKey = eventArg.getClass().getName();
        Set<Class<?>> handlerClasses = eventHandlerContainer.getOrDefault(eventKey, null);
        if (handlerClasses == null) return null;

        Set<IEventHandler> handlers = new LinkedHashSet<>();
        for (Class<?> clazz : handlerClasses) {
            Object handler = SpringContextHolder.getBean(clazz);

            if (handler instanceof IEventHandler) {
                handlers.add((IEventHandler) handler);
            }
        }
        return handlers;
    }
}
