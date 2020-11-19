package com.bird.eventbus.log;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.IEventArg;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventHandleLog extends AbstractEventLog {

    /**
     * 所属消费者组
     */
    private String group;
    /**
     * 接收时间
     */
    private Date receiveTime;
    /**
     * 事件处理状态
     */
    private EventHandleStatusEnum status;
    /**
     * 方法执行日志集合
     */
    private List<EventHandleLog.MethodInvokeLog> items;

    public EventHandleLog(){
        receiveTime = new Date();
        items = new ArrayList<>();
    }

    public EventHandleLog(IEventArg eventArg){
        this();
        this.setEventId(eventArg.getEventId());
        this.setEvent(eventArg.getClass().getName());
        this.setEventJson(JSON.toJSONString(eventArg));
    }

    /**
     * 添加消费结果明细
     * @param item 消费结果
     */
    public void addItem(EventHandleLog.MethodInvokeLog item){
        this.items.add(item);
    }

    @Data
    public class MethodInvokeLog implements Serializable {
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
