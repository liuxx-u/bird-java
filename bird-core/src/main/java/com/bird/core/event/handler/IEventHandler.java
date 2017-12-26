package com.bird.core.event.handler;

import com.bird.core.event.arg.IEventArg;

public interface IEventHandler<T extends IEventArg> {

    void HandleEvent(T eventArg);
}
