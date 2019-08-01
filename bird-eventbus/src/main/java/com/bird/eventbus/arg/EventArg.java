package com.bird.eventbus.arg;


import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author liuxx
 */
@Data
public abstract class EventArg implements IEventArg{

    private String eventId;

    private Date eventTime;

    private Map<String,Object> exts;

    public EventArg(){
        eventId = UUID.randomUUID().toString();
        eventTime = new Date();
        exts = new HashMap<>(4);
    }

    @Override
    public void addExt(String key, Object value) {
        this.exts.put(key,value);
    }

    @Override
    public Object getExt(String key) {
        return this.exts.get(key);
    }
}
