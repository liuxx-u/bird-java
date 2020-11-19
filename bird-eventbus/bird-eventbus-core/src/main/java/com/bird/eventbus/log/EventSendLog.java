package com.bird.eventbus.log;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.IEventArg;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventSendLog extends AbstractEventLog {

    private static final long serialVersionUID = 1L;

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

    public EventSendLog() {
        this.createTime = new Date();
    }

    public EventSendLog(IEventArg eventArg) {
        this();
        this.setEventId(eventArg.getEventId());
        this.setEvent(eventArg.getClass().getName());
        this.setEventJson(JSON.toJSONString(eventArg));
    }
}
