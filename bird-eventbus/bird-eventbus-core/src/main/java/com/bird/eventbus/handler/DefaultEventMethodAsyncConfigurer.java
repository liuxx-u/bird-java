package com.bird.eventbus.handler;

import com.bird.eventbus.arg.IEventArg;

import java.util.concurrent.Executor;

/**
 * @author liuxx
 * @since 2020/11/19
 */
public class DefaultEventMethodAsyncConfigurer implements IEventMethodAsyncConfigurer {

    /**
     * 获取事件方法执行者
     *
     * @param eventArg 事件
     * @return executor
     */
    @Override
    public Executor getAsyncExecutor(IEventArg eventArg) {
        return null;
    }
}
