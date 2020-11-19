package com.bird.eventbus.sender;

import com.bird.eventbus.arg.IEventArg;

/**
 * 事件注册器
 * @author liuxx
 */
public interface IEventSender {

    /**
     * 事件发送
     * @param eventArg 事件参数
     */
    void fire(IEventArg eventArg);
}
