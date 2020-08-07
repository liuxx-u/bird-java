package com.bird.statemachine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public class StateMachineFactory {

    private static Map<String, StateMachine> stateMachineMap = new ConcurrentHashMap<>();

    /**
     * 注册状态机
     * @param stateMachine 状态机
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 用户上下文类型
     */
    public static <S, E, C> void register(StateMachine<S, E, C> stateMachine) {
        String machineId = stateMachine.getMachineId();
        if (stateMachineMap.get(machineId) != null) {
            throw new StateMachineException("The state machine with id [" + machineId + "] is already built, no need to build again");
        }
        stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
    }

    /**
     * 获取状态机
     * @param machineId 状态机id
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 用户上下文类型
     * @return 状态机
     */
    public static <S, E, C> StateMachine<S, E, C> get(String machineId) {
        StateMachine<S, E, C> stateMachine = stateMachineMap.get(machineId);
        if (stateMachine == null) {
            throw new StateMachineException("There is no stateMachine instance for " + machineId + ", please build it first");
        }
        return stateMachine;
    }
}
