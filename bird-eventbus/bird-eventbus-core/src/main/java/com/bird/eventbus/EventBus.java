package com.bird.eventbus;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.sender.IEventSendInterceptor;
import com.bird.eventbus.sender.IEventSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * 事件总线
 * @author liuxx
 */
@Slf4j
public class EventBus {

    @Autowired(required = false)
    private IEventSender eventRegister;

    @Autowired(required = false)
    private Collection<IEventSendInterceptor> interceptors;

    /**
     * 向EventBus中推送消息
     *
     * @param eventArg 事件
     */
    public void push(IEventArg eventArg) {
        if (eventRegister == null) {
            log.warn("未注入IEventRegister，事件发送取消");
            return;
        }
        if (interceptors != null) {
            for (IEventSendInterceptor interceptor : interceptors) {
                interceptor.intercept(eventArg, eventRegister);
            }
        }
        eventRegister.fire(eventArg);
    }
}
