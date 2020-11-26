package com.bird.eventbus.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Data
@ConfigurationProperties(value = "bird.eventbus.log")
public class EventLogProperties {

    /**
     * 触发保存的阈值，默认：100
     */
    private Integer threshold = 100;
    /**
     * 触发保存的时间间隔，默认：20秒
     */
    private Integer period = 20;
    /**
     * 消费线程数，默认：1
     */
    private Short poolSize = 1;
    /**
     * 是否守护线程
     */
    private Boolean daemon = true;
    /**
     * 默认队列长度
     */
    private Integer defaultQueueSize = 10240;
    /**
     * 线程名称
     */
    private String threadNamePattern = "event-log-%d";
}
