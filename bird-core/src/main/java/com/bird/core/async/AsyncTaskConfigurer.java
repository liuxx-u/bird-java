package com.bird.core.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liuxx
 * @since 2020/11/3
 */
@Slf4j
public class AsyncTaskConfigurer implements AsyncConfigurer {

    private final AsyncTaskProperties taskProperties;

    public AsyncTaskConfigurer(AsyncTaskProperties taskProperties) {
        this.taskProperties = taskProperties;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(this.taskProperties.getCorePoolSize());
        //最大线程数
        executor.setMaxPoolSize(this.taskProperties.getMaxPoolSize());
        //队列容量
        executor.setQueueCapacity(this.taskProperties.getQueueCapacity());
        //活跃时间
        executor.setKeepAliveSeconds(this.taskProperties.getKeepAliveSeconds());
        //线程名字前缀
        executor.setThreadNamePrefix("bird-async-");
        //拒绝策略：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> log.error("async task execute error, method:{},message:{}", method.getName(), throwable.getMessage(), throwable);
    }
}
