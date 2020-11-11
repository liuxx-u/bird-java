package com.bird.core.concurrent;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;

/**
 * @author liuxx
 * @since 2020/11/11
 */
public class SchedulerTaskConfigurer implements SchedulingConfigurer {

    private final TaskScheduler taskScheduler;

    public SchedulerTaskConfigurer(TaskScheduler taskScheduler){
        this.taskScheduler = taskScheduler;
    }

    /**
     * Callback allowing a {@link TaskScheduler
     * TaskScheduler} and specific {@link Task Task}
     * instances to be registered against the given the {@link ScheduledTaskRegistrar}.
     *
     * @param taskRegistrar the registrar to be configured.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler);
    }
}
