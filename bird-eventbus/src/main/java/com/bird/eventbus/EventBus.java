package com.bird.eventbus;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.register.IEventRegisterInterceptor;
import com.bird.eventbus.register.IEventRegister;
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
    private IEventRegister eventRegister;

    @Autowired(required = false)
    private Collection<IEventRegisterInterceptor> interceptors;

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
            for (IEventRegisterInterceptor interceptor : interceptors) {
                interceptor.intercept(eventArg, eventRegister);
            }
        }
        eventRegister.regist(eventArg);
    }

    /**
     * 局部事件消费，事件只在本服务内被消费
     *
     * @param eventArg 事件
     */
    public void pushLocal(IEventArg eventArg) {
    }
}
