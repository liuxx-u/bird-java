package com.bird.eventbus.handler;

import com.bird.core.Check;
import com.bird.core.utils.ClassHelper;
import com.bird.core.utils.SpringContextHolder;
import com.bird.eventbus.arg.IEventArg;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 */
@Component
public class EventHandlerFactory {
    /**
     * 重试次数
     */
    private final static short RETRY_COUNT = 3;
    /**
     * 日志记录器
     */
    private static Logger LOGGER = LoggerFactory.getLogger(EventHandlerFactory.class);
    /**
     * topic与处理方法映射关系
     */
    private static Map<String, Set<Method>> EVENT_HANDLER_CONTAINER = new HashMap<>();

    /**
     * 扫描指定包内部的事件监听方法
     *
     * @param packageName
     */
    public static void initWithPackage(String packageName) {
        Check.NotNull(packageName, "packageName");

        Set<Class<?>> classes = ClassHelper.getClasses(packageName);
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
                    if (!EVENT_HANDLER_CONTAINER.containsKey(argClassName)) {
                        EVENT_HANDLER_CONTAINER.put(argClassName, new HashSet<>());
                    }
                    Set eventHandlers = EVENT_HANDLER_CONTAINER.get(argClassName);
                    eventHandlers.add(method);
                    EVENT_HANDLER_CONTAINER.put(argClassName, eventHandlers);
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
        Set<String> keys = EVENT_HANDLER_CONTAINER.keySet();
        //如果开启EventBus，且未处理任何的事件，返回默认topics;
        if (keys.size() == 0) {
            return new String[]{"none-topic"};
        }
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * 事件消费
     *
     * @param eventArg
     * @return
     */

    public static void handleEvent(IEventArg eventArg) {
        String eventKey = eventArg.getClass().getName();
        Set<Method> methods = EVENT_HANDLER_CONTAINER.getOrDefault(eventKey, null);
        if (CollectionUtils.isEmpty(methods)) {
            LOGGER.warn("topic为：" + eventKey + "的事件处理程序不存在.");
            return;
        }

        if (methods.size() == 1) {
            for (Method method : methods) {
                invokeMethod(method, eventArg);
            }
        } else {
            ExecutorService poolExecutor = new ScheduledThreadPoolExecutor(methods.size(), new BasicThreadFactory.Builder().build());
            for (Method method : methods) {
                poolExecutor.execute(() -> invokeMethod(method, eventArg));
            }
            poolExecutor.shutdown();
            try {
                poolExecutor.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                LOGGER.error("awaitTermination", "", e);
            }
        }
    }

    /**
     * 执行事件，执行失败时重试
     * 三次重试执行失败记录错误日志
     *
     * @param method   处理程序方法
     * @param eventArg 事件参数
     */
    private static void invokeMethod(Method method, IEventArg eventArg) {
        short retryCount = RETRY_COUNT;
        boolean success;
        do {
            if (retryCount != RETRY_COUNT) {
                try {
                    Thread.sleep((long) Math.pow(2, RETRY_COUNT - retryCount) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            success = tryInvokeMethod(method, eventArg);
        } while (--retryCount >= 0 && !success);

        if (!success) {
            //TODO:记录时间消费错误日志
        }
    }


    /**
     * 尝试执行事件方法
     *
     * @param method   处理程序方法
     * @param eventArg 事件参数
     * @return 是否执行成功，用于重试判断，只在处理程序方法内部报错时才返回false
     */
    private static boolean tryInvokeMethod(Method method, IEventArg eventArg) {
        Class typeClass = method.getDeclaringClass();
        Object instance = SpringContextHolder.getBean(typeClass);
        if (instance == null) {
            LOGGER.warn("事件消费者：%s未注入spring容器.", typeClass.getName());
            return true;
        }
        try {
            method.invoke(instance, eventArg);
        } catch (IllegalAccessException e) {
            LOGGER.error("%s不能实例化.", typeClass.getName());
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException == null) {
                LOGGER.error("事件消费失败", e);
            } else {
                LOGGER.error(String.format("事件消费失败,事件处理器:%s,message:%s", typeClass.getName(), targetException.getMessage()));
            }
            return false;
        }
        return true;
    }
}
