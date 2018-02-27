package com.bird.eventbus.handler;

import com.bird.eventbus.arg.IEventArg;

public interface IEventHandler<T extends IEventArg> {

    void HandleEvent(T eventArg);
}
