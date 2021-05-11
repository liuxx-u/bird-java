package com.bird.statemachine.test.pojo;

import com.bird.statemachine.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @since 2021/5/11
 */
@Getter
@RequiredArgsConstructor
public enum EventEnum implements Event {

    EVENT1("1"),
    EVENT2("2"),
    EVENT3("3"),
    EVENT4("4");

    private final String name;
}
