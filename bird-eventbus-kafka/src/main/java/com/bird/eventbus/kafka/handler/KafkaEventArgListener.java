package com.bird.eventbus.kafka.handler;

import com.bird.eventbus.arg.EventArg;
import com.bird.eventbus.handler.IEventHandler;
import com.bird.eventbus.handler.IEventHandlerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;

import java.util.Set;

/**
 * kafka事件监听器
 */
public class KafkaEventArgListener implements MessageListener<String,EventArg> {

    @Autowired
    private IEventHandlerFactory eventHandlerFactory;

    @Override
    public void onMessage(ConsumerRecord<String, EventArg> consumerRecord) {
        if (consumerRecord == null) return;
        EventArg value = consumerRecord.value();

        Set<IEventHandler> handlers = eventHandlerFactory.getHandlers(value);
        if (handlers == null) return;
        for (IEventHandler handler : handlers) {
            handler.HandleEvent(value);
        }
    }
}
