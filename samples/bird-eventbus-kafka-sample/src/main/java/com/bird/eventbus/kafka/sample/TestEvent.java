package com.bird.eventbus.kafka.sample;

import com.bird.eventbus.arg.EventArg;
import com.bird.eventbus.handler.EventHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2019/8/28
 */
@Getter
@Setter
public class TestEvent extends EventArg {
    private String name;

    public TestEvent(){}
    public TestEvent(String name){
        this.name = name;
    }
}
