package com.bird.eventbus.handler;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.log.EventHandleLog;
import com.bird.eventbus.log.EventHandleStatusEnum;
import com.bird.eventbus.log.IEventLogDispatcher;
import com.bird.eventbus.registry.IEventRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Slf4j
public class EventMethodInvoker implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final EventHandlerProperties handlerProperties;
    private final IEventMethodExecutor executor;
    private final IEventRegistry eventRegistry;
    private final List<IEventMethodInvokerInterceptor> invokerInterceptors;
    private final IEventLogDispatcher eventLogDispatcher;

    public EventMethodInvoker(EventHandlerProperties handlerProperties
            , IEventMethodExecutor executor
            , IEventRegistry eventRegistry
            , List<IEventMethodInvokerInterceptor> invokerInterceptors
            , IEventLogDispatcher eventLogDispatcher) {
        this.handlerProperties = handlerProperties;
        this.executor = executor;
        this.eventRegistry = eventRegistry;
        this.invokerInterceptors = invokerInterceptors;
        this.eventLogDispatcher = eventLogDispatcher;
    }

    /**
     * 执行事件处理方法
     *
     * @param eventArg 事件
     */
    public void invoke(IEventArg eventArg) {
        if (eventArg == null) {
            log.warn("event arg is null.");
            return;
        }

        this.executor.execute(() -> this.handleEvent(eventArg));
    }

    private void handleEvent(IEventArg eventArg){
        EventHandleLog handleLog = new EventHandleLog(eventArg);
        handleLog.setGroup(this.handlerProperties.getGroup());
        EventHandleStatusEnum status = EventHandleStatusEnum.FAIL;
        int span = 24 * 60 * 60 * 1000;
        if (System.currentTimeMillis() - eventArg.getEventTime().getTime() > span) {
            status = EventHandleStatusEnum.TIMEOUT;
        } else {
            String eventKey = eventArg.getClass().getName();
            Set<Method> methods = this.eventRegistry.getTopicMethods(eventKey);
            if (!CollectionUtils.isEmpty(methods)) {
                int successCount = 0;
                for (Method method : methods) {
                    EventHandleLog.MethodInvokeLog methodInvokeLog = this.invokeMethod(method, eventArg, handleLog);
                    handleLog.addItem(methodInvokeLog);
                    if (methodInvokeLog.getSuccess()) {
                        successCount++;
                    }
                }
                if (successCount >= handleLog.getItems().size()) {
                    status = EventHandleStatusEnum.SUCCESS;
                } else if (successCount > 0) {
                    status = EventHandleStatusEnum.PARTIAL_SUCCESS;
                }
            }
        }
        handleLog.setStatus(status);
        eventLogDispatcher.dispatch(handleLog);
    }

    /**
     * 执行事件处理方法
     *
     * @param method   处理程序方法
     * @param eventArg 事件参数
     */
    private EventHandleLog.MethodInvokeLog invokeMethod(Method method, IEventArg eventArg, EventHandleLog eventHandleLog) {

        EventHandleLog.MethodInvokeLog methodInvokeLog = eventHandleLog.new MethodInvokeLog();

        Class<?> typeClass = method.getDeclaringClass();
        try {
            Object instance = applicationContext.getBean(typeClass);

            this.interceptBefore(method, eventArg);

            method.invoke(instance, eventArg);

            this.interceptAfter(method, eventArg);
            methodInvokeLog.setSuccess(true);
        } catch (InvocationTargetException e) {
            this.interceptException(method, eventArg, e);

            methodInvokeLog.setSuccess(false);
            methodInvokeLog.setMessage(e.getTargetException().getMessage());
            log.error("事件消费失败", e);
        } catch (Exception e) {
            this.interceptException(method, eventArg, e);

            methodInvokeLog.setSuccess(false);
            methodInvokeLog.setMessage(e.getLocalizedMessage());
            log.error("事件消费失败", e);
        }

        methodInvokeLog.setClazz(typeClass.getName());
        methodInvokeLog.setMethod(method.getName());
        return methodInvokeLog;
    }


    /**
     * 执行事件处理前拦截方法
     *
     * @param method   执行的方法
     * @param eventArg 事件消息
     */
    private void interceptBefore(Method method, IEventArg eventArg) {
        if (invokerInterceptors != null) {
            for (IEventMethodInvokerInterceptor interceptor : invokerInterceptors) {
                interceptor.beforeHandle(eventArg, method);
            }
        }
    }

    /**
     * 执行事件处理后拦截方法
     *
     * @param method   执行的方法
     * @param eventArg 事件消息
     */
    private void interceptAfter(Method method, IEventArg eventArg) {
        if (invokerInterceptors != null) {
            for (IEventMethodInvokerInterceptor interceptor : invokerInterceptors) {
                interceptor.afterHandle(eventArg, method);
            }
        }
    }

    /**
     * 执行事件处理异常拦截方法
     *
     * @param method   执行的方法
     * @param eventArg 事件消息
     */
    private void interceptException(Method method, IEventArg eventArg, Exception ex) {
        if (invokerInterceptors != null) {
            for (IEventMethodInvokerInterceptor interceptor : invokerInterceptors) {
                interceptor.onException(eventArg, method, ex);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
