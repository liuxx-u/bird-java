package com.bird.service.zero.event;


import com.bird.eventbus.arg.EventArg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestEventArg extends EventArg {
    private String value;
}
