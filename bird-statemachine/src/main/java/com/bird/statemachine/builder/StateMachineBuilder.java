package com.bird.statemachine.builder;

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
public class StateMachineBuilder<C extends StateContext> {

    private final Map<String, StandardState<C>> stateMap = new ConcurrentHashMap<>();

    private StateMachineBuilder() {
    }

    /**
     * 初始化
     *
     * @param <C> 用户上下文类型
     * @return 状态机构造器
     */
    public static <C extends StateContext> StateMachineBuilder<C> init() {
        return new StateMachineBuilder<>();
    }


    public StandardStateBuilder<C> state() {
        return new StandardStateBuilder<>(stateMap);
    }

    public StandardStatesBuilder<C> states() {
        return new StandardStatesBuilder<>(stateMap);
    }

    public StateMachine<C> build(String machineId) {
        StateMachine<C> stateMachine = new StandardStateMachine<>(machineId, new ArrayList<>(this.stateMap.values()));
        stateMachine.setReady(true);
        StateMachineFactory.register(stateMachine);
        return stateMachine;
    }
}
