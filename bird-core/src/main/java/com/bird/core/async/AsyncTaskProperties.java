package com.bird.core.async;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/11/3
 */
@Data
@ConfigurationProperties(value = "bird.task.pool")
public class AsyncTaskProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = 5;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 5;

    /**
     * 存活时间，单位：秒
     */
    private int keepAliveSeconds = 10;

    /**
     * 队列长度
     */
    private int queueCapacity = Integer.MAX_VALUE;
}
