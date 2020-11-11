package com.bird.core.concurrent;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/11/3
 */
@Data
@ConfigurationProperties(value = "bird.task.executor")
public class TaskExecutorProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = 1;

    /**
     * 最大线程数
     */
    private int maxPoolSize = Integer.MAX_VALUE;

    /**
     * 存活时间，单位：秒
     */
    private int keepAliveSeconds = 60;

    /**
     * 队列长度
     */
    private int queueCapacity = Integer.MAX_VALUE;

    /**
     * 是否守护
     */
    private boolean daemon = false;
}
