package com.bird.statemachine.builder;

import com.bird.statemachine.Event;
import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.factory.StandardState;
import com.bird.statemachine.factory.StandardStateMachine;
import com.bird.statemachine.factory.StateMachine;
import com.bird.statemachine.factory.StateMachineFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxx
 * @since 2021/5/10
 */
public class StateMachineBuilder<S extends State, E extends Event, C extends StateContext> {

    private final Map<String, StandardState<S, C>> stateMap = new ConcurrentHashMap<>();

    private StateMachineBuilder() {
    }

    /**
     * 初始化
     *
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 用户上下文类型
     * @return 状态机构造器
     */
    public static <S extends State, E extends Event, C extends StateContext> StateMachineBuilder<S, E, C> init() {
        return new StateMachineBuilder<>();
    }


    public StandardStateBuilder<S, E, C> state() {
        return new StandardStateBuilder<>(stateMap);
    }

    public StandardStatesBuilder<S, E, C> states() {
        return new StandardStatesBuilder<>(stateMap);
    }

    public StateMachine<S, E, C> build(String machineId) {
        StateMachine<S, E, C> stateMachine = new StandardStateMachine<>(machineId, new ArrayList<>(this.stateMap.values()));
        stateMachine.setReady(true);
        StateMachineFactory.register(stateMachine);
        return stateMachine;
    }
}
