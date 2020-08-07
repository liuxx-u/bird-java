package com.bird.statemachine.builder;

import com.bird.statemachine.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public class StateMachineBuilder<S,E,C> {

    private final Map<S, State< S, E, C>> stateMap = new ConcurrentHashMap<>();

    private StateMachineBuilder() {
    }

    /**
     * 初始化
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 用户上下文类型
     * @return 状态机构造器
     */
    public static <S, E, C> StateMachineBuilder<S, E, C> init() {
        return new StateMachineBuilder<>();
    }


    public TransitionBuilder<S, E, C> externalTransition() {
        return new TransitionBuilder<>(stateMap, TransitionType.EXTERNAL);
    }

    public TransitionsBuilder<S, E, C> externalTransitions() {
        return new TransitionsBuilder<>(stateMap, TransitionType.EXTERNAL);
    }

    public TransitionBuilder<S, E, C> internalTransition() {
        return new TransitionBuilder<>(stateMap, TransitionType.INTERNAL);
    }

    public StateMachine<S, E, C> build(String machineId) {
        StateMachine<S,E,C> stateMachine = new StandardStateMachine<>(machineId,stateMap);
        stateMachine.setReady(true);
        StateMachineFactory.register(stateMachine);
        return stateMachine;
    }
}
