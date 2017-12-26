package com.bird.core.event.register;

import com.bird.core.event.arg.IEventArg;

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
