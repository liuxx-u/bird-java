package com.bird.eventbus.handler;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import com.bird.eventbus.arg.IEventArg;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author liuxx
 * @since 2020/11/19
 */
public class DefaultEventMethodAsyncConfigurer implements IEventMethodAsyncConfigurer {

    private final ThreadPoolTaskExecutor taskExecutor;

    public DefaultEventMethodAsyncConfigurer(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * 获取事件方法执行者
     *
     * @param eventArg 事件
     * @return executor
     */
    @Override
    public Executor getAsyncExecutor(IEventArg eventArg) {
        return this.taskExecutor;
    }
}
