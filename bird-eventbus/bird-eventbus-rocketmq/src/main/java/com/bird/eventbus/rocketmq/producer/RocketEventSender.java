package com.bird.eventbus.rocketmq.producer;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.log.EventSendLog;
import com.bird.eventbus.log.IEventLogDispatcher;
import com.bird.eventbus.sender.IEventSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author liuxx
 * @since 2020/11/25
 */
@Slf4j
public class RocketEventSender implements IEventSender {

    private final RocketMQTemplate rocketTemplate;
    private final IEventLogDispatcher eventLogDispatcher;

    public RocketEventSender(RocketMQTemplate rocketTemplate, IEventLogDispatcher eventLogDispatcher) {
        this.rocketTemplate = rocketTemplate;
        this.eventLogDispatcher = eventLogDispatcher;
    }

    /**
     * 事件发送
     *
     * @param eventArg 事件参数
     */
    @Override
    public void fire(IEventArg eventArg) {
        Message<?> message = MessageBuilder.withPayload(eventArg).build();

        EventSendLog sendLog = new EventSendLog(eventArg);
        rocketTemplate.asyncSend(this.getTopic(eventArg), message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (eventLogDispatcher == null) {
                    return;
                }
                sendLog.setSuccess(true);
                sendLog.setExtJson(JSON.toJSONString(sendResult));
                eventLogDispatcher.dispatch(sendLog);
            }

            @Override
            public void onException(Throwable e) {
                if (eventLogDispatcher == null) {
                    return;
                }
                sendLog.setSuccess(false);
                sendLog.setMessage(e.getMessage());
                eventLogDispatcher.dispatch(sendLog);
                log.error(e.getMessage());
            }
        });
    }

    private String getTopic(IEventArg eventArg) {
        return StringUtils.replace(eventArg.getClass().getName(), ".", "-");
    }
}
