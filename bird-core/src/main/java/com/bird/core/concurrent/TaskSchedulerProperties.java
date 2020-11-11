package com.bird.core.concurrent;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/11/11
 */
@Data
@ConfigurationProperties(value = "bird.task.scheduler")
public class TaskSchedulerProperties {

    /**
     * 线程数
     */
    private int poolSize = 1;

    /**
     * remove-on-cancel
     */
    private boolean removeOnCancelPolicy = false;

    /**
     * daemon
     */
    private boolean daemon = false;
}
