package com.bird.eventbus.register;

import com.bird.eventbus.arg.IEventArg;

/**
 * 事件注册器
 */
public interface IEventRegister {

    /**
     * 事件注册
     * @param eventArg 事件参数
     */
    void regist(IEventArg eventArg);
}
