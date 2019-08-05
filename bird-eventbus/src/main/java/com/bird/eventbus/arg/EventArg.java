package com.bird.eventbus.arg;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

/**
 * @author liuxx
 */
@Data
public abstract class EventArg implements IEventArg{

    private String eventId;

    private Date eventTime;

    public EventArg(){
        eventId = UUID.randomUUID().toString();
        eventTime = new Date();
    }
}
