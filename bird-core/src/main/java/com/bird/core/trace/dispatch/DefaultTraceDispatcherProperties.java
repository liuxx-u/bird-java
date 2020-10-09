package com.bird.core.trace.dispatch;

import lombok.Data;

/**
 * @author liuxx
 * @since 2020/10/9
 */
@Data
public class DefaultTraceDispatcherProperties {

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
}
