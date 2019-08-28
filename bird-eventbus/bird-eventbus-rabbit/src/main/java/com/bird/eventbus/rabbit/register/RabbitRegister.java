package com.bird.eventbus.rabbit.register;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.register.IEventRegister;
import com.bird.eventbus.register.IEventRegisterStore;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liuxx
 * @date 2019/1/18
 */
@Slf4j
public class RabbitRegister implements IEventRegister,RabbitTemplate.ConfirmCallback {

    @Autowired(required = false)
    private IEventRegisterStore registerStore;

    final RabbitTemplate rabbitTemplate;

    public RabbitRegister(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void regist(IEventArg eventArg) {
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
