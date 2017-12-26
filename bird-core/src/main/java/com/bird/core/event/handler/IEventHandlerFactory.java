package com.bird.core.event.handler;

import com.bird.core.event.arg.IEventArg;

import java.util.Set;

public interface IEventHandlerFactory {

    /**
     * 获取事件处理器
     * @return
     */
    Set<IEventHandler> getHandlers(IEventArg eventArg);
}
