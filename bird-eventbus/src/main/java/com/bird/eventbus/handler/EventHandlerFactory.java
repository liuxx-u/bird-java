package com.bird.eventbus.handler;

import com.bird.eventbus.arg.IEventArg;
import com.bird.core.utils.ClassHelper;
import com.bird.core.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
public class EventHandlerFactory {

    private static Map<String, Set<Method>> eventHandlerContainer = new HashMap<>();

    /**
     * 初始化事件处理容器
     */
    static {
        Set<Class<?>> classes = ClassHelper.getClasses("com.bird");
        if (classes != null) {
            for (Class<?> clazz : classes) {
                for (Method method : clazz.getDeclaredMethods()) {
                    EventHandler eventAnnotation = method.getAnnotation(EventHandler.class);
                    if (eventAnnotation == null) continue;

                    //被@EventHandler注解标注的方法只接受一个参数，且参数必须是IEventArg的子类
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) continue;
                    if (!IEventArg.class.isAssignableFrom(parameterTypes[0])) continue;

                    String argClassName = parameterTypes[0].getName();
                    if (!eventHandlerContainer.containsKey(argClassName)) {
                        eventHandlerContainer.put(argClassName, new LinkedHashSet<>());
                    }
                    Set eventHandlers = eventHandlerContainer.get(argClassName);
                    eventHandlers.add(method);
                    eventHandlerContainer.put(argClassName, eventHandlers);
                }
            }
        }
    }

    /**
     * 获取当前程序所有事件的名称
     *
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

    public static void handleEvent(IEventArg eventArg) {
        String eventKey = eventArg.getClass().getName();
        Set<Method> methods = eventHandlerContainer.getOrDefault(eventKey, null);

        if (methods == null) return;
        for (Method method : methods) {
            Object instance = SpringContextHolder.getBean(method.getDeclaringClass());
            if (instance == null) continue;
            try {
                method.invoke(instance, eventArg);
            } catch (InvocationTargetException ex) {
            } catch (IllegalAccessException ex) {
            }
        }

    }
}
