package com.bird.eventbus.handler;

import com.bird.eventbus.arg.IEventArg;

import java.util.Set;

public interface IEventHandlerFactory {

    /**
     * 获取事件处理器
     * @return
     */
    Set<IEventHandler> getHandlers(IEventArg eventArg);
}
