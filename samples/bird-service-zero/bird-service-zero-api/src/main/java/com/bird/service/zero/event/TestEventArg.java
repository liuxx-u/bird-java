package com.bird.service.zero.event;


import com.bird.eventbus.arg.EventArg;

public class TestEventArg extends EventArg {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
