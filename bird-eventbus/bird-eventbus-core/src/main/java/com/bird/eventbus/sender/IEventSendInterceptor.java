package com.bird.eventbus.sender;

import com.bird.eventbus.arg.IEventArg;

/**
 * 事件注册拦截器，事件发送前对事件的处理
 *
 * @author liuxx
 * @date 2019/7/31
 */
public interface IEventSendInterceptor {

    /**
     * 事件发送拦截处理方法
     * @param eventArg 事件参数
     * @param eventRegister 发送器
     */
    void intercept(IEventArg eventArg, IEventSender eventRegister);
}
