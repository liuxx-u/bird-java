package com.bird.eventbus.handler;

import com.bird.eventbus.arg.IEventArg;

import java.lang.reflect.Method;

/**
 * 事件处理拦截器，事件处理前对事件的处理
 *
 * @author liuxx
 * @date 2019/7/31
 */
public interface IEventHandlerInterceptor {

    /**
     * 事件处理前置执行方法
     * @param eventArg 事件参数
     * @param method 执行方法
     */
    void beforeHandle(IEventArg eventArg, Method method);

    /**
     * 事件处理后置执行方法
     * @param eventArg 事件参数
     * @param method 执行方法
     */
    void afterHandle(IEventArg eventArg, Method method);

    /**
     * 事件处理异常时执行方法
     * @param eventArg 事件参数
     * @param method 执行方法
     * @param ex 异常信息
     */
    void onException(IEventArg eventArg, Method method,Exception ex);
}
