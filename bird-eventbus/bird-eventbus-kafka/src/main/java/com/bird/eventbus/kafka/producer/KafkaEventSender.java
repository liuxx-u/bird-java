package com.bird.eventbus.kafka.producer;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.log.EventSendLog;
import com.bird.eventbus.log.IEventLogDispatcher;
import com.bird.eventbus.sender.IEventSender;
import lombok.extern.slf4j.Slf4j;
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
public class KafkaEventSender implements IEventSender {

    private final IEventLogDispatcher eventLogDispatcher;
    private final KafkaTemplate<String, IEventArg> kafkaTemplate;

    public KafkaEventSender(IEventLogDispatcher eventLogDispatcher, KafkaTemplate<String, IEventArg> kafkaTemplate) {
        this.eventLogDispatcher = eventLogDispatcher;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 事件注册
     *
     * @param eventArg 事件参数
     */
    @Override
    public void fire(IEventArg eventArg) {
        ListenableFuture<SendResult<String, IEventArg>> listenableFuture = kafkaTemplate.send(getTopic(eventArg), eventArg);

        EventSendLog sendLog = new EventSendLog(eventArg);
        //发送成功回调
        SuccessCallback<SendResult<String, IEventArg>> successCallback = result -> {
            if (eventLogDispatcher == null) {
                return;
            }
            sendLog.setSuccess(true);
            Map<String, Object> map = new HashMap<>(2);
            if (result != null) {
                map.put("producerRecord", result.getProducerRecord());
                map.put("metadata", result.getRecordMetadata());
            }
            sendLog.setExtJson(JSON.toJSONString(map));
            this.eventLogDispatcher.dispatch(sendLog);
        };

        // 发送失败回调
        FailureCallback failureCallback = ex -> {
            if (eventLogDispatcher == null) {
                return;
            }
            sendLog.setSuccess(false);
            sendLog.setMessage(ex.getMessage());
            this.eventLogDispatcher.dispatch(sendLog);
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
}
