package com.bird.eventbus;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.handler.EventMethodInvoker;
import com.bird.eventbus.sender.IEventSendInterceptor;
import com.bird.eventbus.sender.IEventSender;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * 事件总线
 * @author liuxx
 */
@Slf4j
public class EventBus {

    private final IEventSender eventSender;
    private final EventMethodInvoker eventMethodInvoker;
    private final Collection<IEventSendInterceptor> interceptors;

    public EventBus(IEventSender eventSender, EventMethodInvoker eventMethodInvoker, Collection<IEventSendInterceptor> interceptors) {
        this.eventSender = eventSender;
        this.eventMethodInvoker = eventMethodInvoker;
        this.interceptors = interceptors;
    }

    /**
     * 向EventBus中推送消息
     *
     * @param eventArg 事件
     */
    public void push(IEventArg eventArg) {
        if (interceptors != null) {
            for (IEventSendInterceptor interceptor : interceptors) {
                interceptor.intercept(eventArg, eventSender);
            }
        }

        if (eventArg.isLocal()) {
            if (this.eventMethodInvoker == null) {
                log.warn("未注入事件处理程序，事件处理取消");
                return;
            }
            this.eventMethodInvoker.invoke(eventArg);
        } else {
            if (eventSender == null) {
                log.warn("未注入IEventSender，事件发送取消");
                return;
            }
            eventSender.fire(eventArg);
        }
    }
}
