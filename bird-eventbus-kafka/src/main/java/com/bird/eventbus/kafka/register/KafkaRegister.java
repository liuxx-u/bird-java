package com.bird.eventbus.kafka.register;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.register.IEventRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * kafka事件注册器，向kafka队列中push消息
 */
public class KafkaRegister implements IEventRegister {

    private KafkaTemplate<String,IEventArg> kafkaTemplate;

    /**
     * 事件注册
     *
     * @param eventArg 事件参数
     */
    @Override
    public void regist(IEventArg eventArg) {
        kafkaTemplate.send(getTopic(eventArg),eventArg);
    }

    /**
     * 获取kafka的topic
     *
     *
     * @param eventArg
     * @return topic
     */
    private String getTopic(IEventArg eventArg){
        return eventArg.getClass().getName();
    }

    public void setKafkaTemplate(KafkaTemplate<String, IEventArg> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
