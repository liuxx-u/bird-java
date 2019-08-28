package com.bird.eventbus.register;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.IEventArg;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuxx
 * @date 2019/1/16
 */
@Data
public class EventRegisterResult implements Serializable {
    /**
     * 事件id
     */
    private String eventId;
    /**
     * 事件名称
     */
    private String event;
    /**
     * 事件json数据
     */
    private String eventJson;
    /**
     * 队列信息json数据
     */
    private String extJson;
    /**
     * 是否处理成功
     */
    private Boolean success;
    /**
     * 消息
     */
    private String message;
    /**
     * 记录时间
     */
    private Date createTime;

    public EventRegisterResult() {
        this.createTime = new Date();
    }

    public EventRegisterResult(IEventArg eventArg) {
        this();
        this.eventId = eventArg.getEventId();
        this.event = eventArg.getClass().getName();
        this.eventJson = JSON.toJSONString(eventArg);
    }
}
