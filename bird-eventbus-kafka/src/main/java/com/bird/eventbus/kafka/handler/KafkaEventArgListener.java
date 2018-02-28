package com.bird.eventbus.kafka.handler;

import com.bird.eventbus.arg.EventArg;
import com.bird.eventbus.handler.EventHandlerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * kafka事件监听器
 */
public class KafkaEventArgListener implements MessageListener<String,EventArg> {

    @Override
    public void onMessage(ConsumerRecord<String, EventArg> consumerRecord) {
        if (consumerRecord == null) return;
        EventArg value = consumerRecord.value();

        EventHandlerFactory.handleEvent(value);
    }
}
