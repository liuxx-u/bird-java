package com.bird.eventbus.arg;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

/**
 * 事件参数
 *
 * @author liuxx
 * @since 2021/2/22
 */
@Data
public abstract class EventArg implements IEventArg{

    private static final long serialVersionUID = 1L;

    private String eventId;

    private Date eventTime;

    public EventArg(){
        eventId = UUID.randomUUID().toString();
        eventTime = new Date();
    }
}
