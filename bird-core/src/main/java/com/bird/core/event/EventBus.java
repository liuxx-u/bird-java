package com.bird.core.event;

import com.bird.core.event.arg.IEventArg;
import com.bird.core.event.handler.IEventHandler;
import com.bird.core.event.handler.IEventHandlerFactory;
import com.bird.core.event.register.IEventRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 事件总线
 */
@Component
public class EventBus {

    @Autowired
    private IEventRegister eventRegister;

    /**
     * 向EventBus中推消息
     */
    public void push(IEventArg eventArg){
        eventRegister.regist(eventArg);
    }
}
