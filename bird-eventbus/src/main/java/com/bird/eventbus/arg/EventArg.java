package com.bird.eventbus.arg;


import java.util.Date;

/**
 * @author liuxx
 */
public abstract class EventArg implements IEventArg{

    private Date eventTime;

    public EventArg(){
        eventTime = new Date();
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
}
