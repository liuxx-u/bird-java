package com.bird.eventbus.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.context.SmartLifecycle;

import java.util.Objects;

/**
 * @author liuxx
 * @since 2020/11/25
 */
@Slf4j
public class RocketEventConsumerContainer implements SmartLifecycle {

    private boolean running;
    private DefaultMQPushConsumer consumer;

    public RocketEventConsumerContainer(String consumerGroup,RocketEventHandlerProperties rocketProperties,MessageListenerConcurrently listenerConcurrently){

        this.consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(rocketProperties.getNameServer());
        consumer.setConsumeThreadMin(rocketProperties.getConsumeThreadMin());
        consumer.setConsumeThreadMax(rocketProperties.getConsumeThreadMax());
        consumer.setConsumeTimeout(rocketProperties.getConsumeTimeout());
        consumer.setMessageListener(listenerConcurrently);
    }

    public void initialize(String[] topics) throws MQClientException {

        for (String topic : topics) {
            if (StringUtils.isBlank(topic)) {
                continue;
            }
            consumer.subscribe(StringUtils.replace(topic, ".", "-"), "*");
        }
    }


    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public void start() {
        if (this.isRunning()) {
            throw new IllegalStateException("container already running. " + this.toString());
        }

        try {
            consumer.start();
        } catch (MQClientException e) {
            throw new IllegalStateException("Failed to start RocketMQ push consumer", e);
        }
        this.running = true;

        log.info("running container: {}", this.toString());
    }

    @Override
    public void stop() {
        if (this.isRunning()) {
            if (Objects.nonNull(consumer)) {
                consumer.shutdown();
            }
            this.running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
