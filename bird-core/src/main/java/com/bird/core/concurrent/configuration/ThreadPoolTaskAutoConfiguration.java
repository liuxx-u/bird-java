package com.bird.core.concurrent.configuration;

import com.bird.core.concurrent.AsyncTaskConfigurer;
import com.bird.core.concurrent.SchedulerTaskConfigurer;
import com.bird.core.concurrent.TaskExecutorProperties;
import com.bird.core.concurrent.TaskSchedulerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liuxx
 * @since 2020/11/4
 */
@Configuration
@EnableConfigurationProperties({TaskExecutorProperties.class, TaskSchedulerProperties.class})
public class ThreadPoolTaskAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "bird.task.executor.core-pool-size")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(TaskExecutorProperties executorProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(executorProperties.getCorePoolSize());
        //最大线程数
        executor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        //队列容量
        executor.setQueueCapacity(executorProperties.getQueueCapacity());
        //活跃时间
        executor.setKeepAliveSeconds(executorProperties.getKeepAliveSeconds());
        //线程名字前缀
        executor.setThreadNamePrefix("bird-task-executor-");
        // 是否守护线程
        executor.setDaemon(executorProperties.isDaemon());
        //拒绝策略：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnProperty(value = "bird.task.executor.core-pool-size")
    public AsyncConfigurer asyncConfigurer(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return new AsyncTaskConfigurer(threadPoolTaskExecutor);
    }

    @Bean
    @ConditionalOnProperty(value = "bird.task.scheduler.pool-size")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(TaskSchedulerProperties schedulerProperties) {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        //核心线程池大小
        executor.setPoolSize(schedulerProperties.getPoolSize());
        executor.setRemoveOnCancelPolicy(schedulerProperties.isRemoveOnCancelPolicy());
        //线程名字前缀
        executor.setThreadNamePrefix("bird-task-scheduler-");
        // 是否守护线程
        executor.setDaemon(executor.isDaemon());
        //拒绝策略：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnProperty(value = "bird.task.executor.core-pool-size")
    public SchedulingConfigurer schedulingConfigurer(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        return new SchedulerTaskConfigurer(threadPoolTaskScheduler);
    }
}
