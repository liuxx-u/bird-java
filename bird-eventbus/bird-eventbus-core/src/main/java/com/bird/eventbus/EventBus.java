package com.bird.eventbus;

import com.bird.eventbus.arg.IEventArg;
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
    private final Collection<IEventSendInterceptor> interceptors;

    public EventBus(IEventSender eventSender, Collection<IEventSendInterceptor> interceptors){
        this.eventSender = eventSender;
        this.interceptors = interceptors;
    }

    /**
     * 向EventBus中推送消息
     *
     * @param eventArg 事件
     */
    public void push(IEventArg eventArg) {
        if (eventSender == null) {
            log.warn("未注入IEventRegister，事件发送取消");
            return;
        }
        if (interceptors != null) {
            for (IEventSendInterceptor interceptor : interceptors) {
                interceptor.intercept(eventArg, eventSender);
            }
        }
        eventSender.fire(eventArg);
    }
}
