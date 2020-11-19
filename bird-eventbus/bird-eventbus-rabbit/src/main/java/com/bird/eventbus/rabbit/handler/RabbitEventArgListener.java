package com.bird.eventbus.rabbit.handler;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.handler.EventMethodInvoker;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.nio.charset.Charset;

/**
 * @author liuxx
 * @date 2019/1/18
 */
@Slf4j
public class RabbitEventArgListener implements ChannelAwareMessageListener {

    private EventMethodInvoker eventMethodInvoker;

    public RabbitEventArgListener(EventMethodInvoker eventMethodInvoker) {
        this.eventMethodInvoker = eventMethodInvoker;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Class<?> clazz;
        String className = message.getMessageProperties().getReceivedExchange();
        try {
            clazz = Class.forName(className);
            if (!IEventArg.class.isAssignableFrom(clazz)) {
                log.error("事件处理失败：" + className + "不是IEventArg的子类");
            }
        } catch (ClassNotFoundException ex) {
            log.error("事件处理失败：", ex);
            return;
        }

        String body = new String(message.getBody(), Charset.forName("utf8"));
        IEventArg eventArg = (IEventArg) JSON.parseObject(body,clazz);

        eventMethodInvoker.invoke(eventArg);

        //确认消息成功消费
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
