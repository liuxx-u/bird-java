package com.bird.eventbus.rocketmq.consumer;

import lombok.Data;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/11/25
 */
@Data
@ConfigurationProperties("bird.eventbus.handler.rocketmq")
public class RocketEventHandlerProperties {

    /**
     * RocketMQ Name Server
     */
    private String nameServer;
    /**
     * 最小消费线程数
     */
    private Integer consumeThreadMin = 20;
    /**
     * 最大消费线程数
     */
    private Integer consumeThreadMax = 20;
    /**
     * 消费超时时间，单位：分钟
     */
    private Integer consumeTimeout = 15;
    /**
     * 消费模式：默认：集群消费
     */
    private MessageModel messageModel = MessageModel.CLUSTERING;
    /**
     * 消费者重试策略 <br>
     * -1,no retry,put into DLQ directly<br>
     * 0,broker control retry frequency<br>
     * >0,client control retry frequency
     */
    private Integer delayLevelWhenNextConsume = 0;
}
