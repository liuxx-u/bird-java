package com.bird.eventbus.handler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
    /**
     * 消费日志存储配置
     */
    @NestedConfigurationProperty
    private EventHandleLogProperties logConfig;

    @Data
    private static class EventHandleLogProperties {

        /**
         * 批量保存阈值
         */
        private Integer threshold = 100;
        /**
         * 批量保存间隔，单位：秒
         */
        private Integer period;
    }
}
