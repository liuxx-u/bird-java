package com.bird.eventbus.handler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Data
@ConfigurationProperties(value = "bird.eventbus.handler")
public class EventHandlerProperties {

    /**
     * 消费者组
     */
    private String group;
    /**
     * 事件处理方法扫描路径
     */
    private String scanPackages;
}
