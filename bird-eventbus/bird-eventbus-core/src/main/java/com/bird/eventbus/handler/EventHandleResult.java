package com.bird.eventbus.handler;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.IEventArg;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/1/16
 */
@Data
public class EventHandleResult implements Serializable {

    /**
     * 事件id
     */
    private String eventId;
    /**
     * 事件名称
     */
    private String event;
    /**
     * 所属消费者组
     */
    private String group;
    /**
     * 事件参数JSON结果
     */
    private String eventJson;
    /**
     * 接收时间
     */
    private Date receiveTime;
    /**
     * 事件处理状态
     */
    private EventHandleStatusEnum status;
    /**
     * 消费者处理结果集合
     */
    private List<ConsumerResult> items;

    public EventHandleResult(){
        receiveTime = new Date();
        items = new ArrayList<>();
    }

    public EventHandleResult(IEventArg eventArg){
        this();
        this.eventId = eventArg.getEventId();
        this.event = eventArg.getClass().getName();
        this.eventJson = JSON.toJSONString(eventArg);
    }

    /**
     * 添加消费结果明细
     * @param item 消费结果
     */
    public void addItem(ConsumerResult item){
        this.items.add(item);
    }

    @Data
    public class ConsumerResult implements Serializable{
        /**
         * 是否成功
         */
        private Boolean success;
        /**
         * 错误消息
         */
        private String message;
        /**
         * 监听类名称
         */
        private String clazz;
        /**
         * 事件监听方法
         */
        private String method;
    }
}
