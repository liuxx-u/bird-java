package com.bird.eventbus.rabbit.register;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.sender.IEventSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author liuxx
 * @date 2019/1/18
 */
@Slf4j
public class RabbitEventSender implements IEventSender,RabbitTemplate.ConfirmCallback {


    private final RabbitTemplate rabbitTemplate;

    public RabbitEventSender(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void fire(IEventArg eventArg) {
        CorrelationData data = new CorrelationData();
        data.setId(eventArg.getEventId());

        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend(eventArg.getClass().getName(), "", eventArg, data);
    }

    @Override
    public void confirm(CorrelationData data, boolean success, String s) {

        if (success) {

        } else {

        }
    }
}
