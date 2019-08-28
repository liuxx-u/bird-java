package com.bird.eventbus.kafka.register;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.register.IEventRegister;
import com.bird.eventbus.register.EventRegisterResult;
import com.bird.eventbus.register.IEventRegisterStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * kafka事件注册器，向kafka队列中push消息
 * @author liuxx
 */
@Slf4j
public class KafkaRegister implements IEventRegister {

    @Autowired(required = false)
    private IEventRegisterStore registerStore;

    private KafkaTemplate<String, IEventArg> kafkaTemplate;

    /**
     * 事件注册
     *
     * @param eventArg 事件参数
     */
    @Override
    public void regist(IEventArg eventArg) {
        ListenableFuture<SendResult<String, IEventArg>> listenableFuture = kafkaTemplate.send(getTopic(eventArg), eventArg);

        EventRegisterResult registerResult = new EventRegisterResult(eventArg);
        //发送成功回调
        SuccessCallback<SendResult<String, IEventArg>> successCallback = result -> {
            if (registerStore == null) return;
            registerResult.setSuccess(true);
            Map<String, Object> map = new HashMap<>(2);
            if (result != null) {
                map.put("producerRecord", result.getProducerRecord());
                map.put("metadata", result.getRecordMetadata());
            }
            registerResult.setExtJson(JSON.toJSONString(map));
            registerStore.register(registerResult);
        };

        //发送失败回调
        FailureCallback failureCallback = ex -> {
            if (registerStore == null) return;
            registerResult.setSuccess(false);
            registerResult.setMessage(ex.getMessage());
            registerStore.register(registerResult);

            log.error(ex.getMessage());
        };
        listenableFuture.addCallback(successCallback, failureCallback);
    }

    /**
     * 获取kafka的topic
     *
     * @param eventArg 事件消息
     * @return topic topic
     */
    private String getTopic(IEventArg eventArg) {
        return eventArg.getClass().getName();
    }

    public void setKafkaTemplate(KafkaTemplate<String, IEventArg> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
