package com.bird.eventbus.arg;


import lombok.Getter;

import java.util.Date;

/**
 * @author liuxx
 */
@Getter
public abstract class EventArg implements IEventArg{

    private Date eventTime;

    public EventArg(){
        eventTime = new Date();
    }
}
