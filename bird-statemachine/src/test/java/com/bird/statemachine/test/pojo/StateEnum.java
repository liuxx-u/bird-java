package com.bird.statemachine.test.pojo;

import com.bird.statemachine.State;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @since 2021/5/11
 */
@Getter
@RequiredArgsConstructor
public enum StateEnum implements State {

    STATE1("1"),
    STATE2("2"),
    STATE3("3"),
    STATE4("4");

    private final String name;
}
