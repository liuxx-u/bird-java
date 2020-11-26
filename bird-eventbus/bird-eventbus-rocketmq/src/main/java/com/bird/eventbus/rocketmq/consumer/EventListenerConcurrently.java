package com.bird.eventbus.rocketmq.consumer;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.handler.EventMethodInvoker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxx
 * @since 2020/11/25
 */
@Slf4j
public class EventListenerConcurrently implements MessageListenerConcurrently {

    private final static ConcurrentHashMap<String,Class<?>> TOPIC_EVENT_CLASS_MAP = new ConcurrentHashMap<>();

    private final static String UTF_8 = "UTF-8";

    private final RocketEventHandlerProperties rocketProperties;
    private final EventMethodInvoker eventMethodInvoker;
    private final MessageConverter messageConverter;

    public EventListenerConcurrently(RocketEventHandlerProperties rocketProperties,EventMethodInvoker eventMethodInvoker,MessageConverter messageConverter){
        this.rocketProperties = rocketProperties;
        this.eventMethodInvoker = eventMethodInvoker;
        this.messageConverter = messageConverter;
    }

    /**
     * It is not recommend to throw exception,rather than returning ConsumeConcurrentlyStatus.RECONSUME_LATER if
     * consumption failure
     *
     * @param msgs    msgs.size() >= 1<br> DefaultMQPushConsumer.consumeMessageBatchMaxSize=1,you can modify here
     * @param context context
     * @return The consume status
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt messageExt : msgs) {
            log.debug("received msg: {}", messageExt);
            try {
                long now = System.currentTimeMillis();
                handleMessage(messageExt);
                long costTime = System.currentTimeMillis() - now;
                log.debug("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
            } catch (Exception e) {
                log.warn("consume message failed. messageExt:{}, error:{}", messageExt, e);
                context.setDelayLevelWhenNextConsume(this.rocketProperties.getDelayLevelWhenNextConsume());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    private void handleMessage(MessageExt messageExt) {
        IEventArg eventArg = this.convertMessage(messageExt);
        if(eventArg != null){
            this.eventMethodInvoker.invoke(eventArg);
        }
    }

    private IEventArg convertMessage(MessageExt messageExt) {
        String str = new String(messageExt.getBody(), Charset.forName(UTF_8));
        Class<?> eventClass = this.getEventClass(messageExt);
        if (eventClass == null) {
            return null;
        }

        Object event = this.messageConverter.fromMessage(MessageBuilder.withPayload(str).build(), eventClass);
        if (event instanceof IEventArg) {
            return (IEventArg) event;
        } else {
            log.warn("消息序列化为IEventArg实例出错,message:", messageExt);
            return null;
        }
    }

    private Class<?> getEventClass(MessageExt messageExt){
        String topic = messageExt.getTopic();

        return TOPIC_EVENT_CLASS_MAP.computeIfAbsent(topic,t->{
            String className = StringUtils.replace(topic,"-",".");
            try {
                Class<?> clazz = Class.forName(className);
                return IEventArg.class.isAssignableFrom(clazz) ? clazz : null;
            } catch (Exception e) {
                return null;
            }
        });
    }
}
