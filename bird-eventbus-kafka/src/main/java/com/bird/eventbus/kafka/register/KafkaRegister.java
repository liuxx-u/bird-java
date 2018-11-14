package com.bird.eventbus.kafka.register;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.register.IEventRegister;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * kafka事件注册器，向kafka队列中push消息
 * @author liuxx
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
        ListenableFuture<SendResult<String, IEventArg>> listenableFuture = kafkaTemplate.send(getTopic(eventArg),eventArg);

        //发送成功回调
        SuccessCallback<SendResult<String, IEventArg>> successCallback = result -> {

        };

        //发送失败回调
        FailureCallback failureCallback = ex -> {

        };
        listenableFuture.addCallback(successCallback, failureCallback);
    }

    /**
     * 获取kafka的topic
     *
     *
     * @param eventArg 事件消息
     * @return topic topic
     */
    private String getTopic(IEventArg eventArg){
        return eventArg.getClass().getName();
    }

    public void setKafkaTemplate(KafkaTemplate<String, IEventArg> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
