package com.bird.eventbus.rabbit.consumer;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.handler.EventMethodInvoker;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import java.io.IOException;

/**
 * @author liuxx
 * @date 2019/1/18
 */
@Slf4j
public class RabbitEventArgListener implements ChannelAwareMessageListener {

    private EventMethodInvoker eventMethodInvoker;
    private MessageConverter messageConverter;

    public RabbitEventArgListener(EventMethodInvoker eventMethodInvoker) {
        this.eventMethodInvoker = eventMethodInvoker;
        this.messageConverter = new SimpleMessageConverter();
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        Class<?> clazz;
        String className = message.getMessageProperties().getReceivedExchange();
        try {
            clazz = Class.forName(className);
            if (IEventArg.class.isAssignableFrom(clazz)) {
                IEventArg eventArg = (IEventArg) messageConverter.fromMessage(message);
                eventMethodInvoker.invoke(eventArg);

                long deliveryTag = message.getMessageProperties().getDeliveryTag();
                channel.basicAck(deliveryTag, true);
            }
        } catch (ClassNotFoundException ex) {
            log.error("事件处理失败：", ex);
        } catch (IOException ex) {
            log.error("消息处理状态回执失败：", ex);
        }
    }
}
