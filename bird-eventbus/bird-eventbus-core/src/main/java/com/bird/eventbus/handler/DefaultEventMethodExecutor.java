package com.bird.eventbus.handler;

import java.util.concurrent.RejectedExecutionException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author liuxx
 * @since 2020/11/19
 */
public class DefaultEventMethodExecutor implements IEventMethodExecutor {

    private final ThreadPoolTaskExecutor taskExecutor;

    public DefaultEventMethodExecutor(ThreadPoolTaskExecutor taskExecutor){
        this.taskExecutor = taskExecutor;
    }

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     *                                    accepted for execution
     * @throws NullPointerException       if command is null
     */
    @Override
    public void execute(Runnable command) {
        this.taskExecutor.execute(command);
    }
}
