package com.bird.statemachine;

import java.util.Map;
import java.util.Optional;

/**
 * @author liuxx
 * @since 2020/8/6
 */
public class StandardStateMachine<S,E,C> implements StateMachine<S,E,C> {

    private final String machineId;
    private final Map<S, State<S, E, C>> stateMap;

    private boolean ready;

    public StandardStateMachine(String machineId, Map<S, State<S, E, C>> stateMap) {
        this.machineId = machineId;
        this.stateMap = stateMap;
    }

    /**
     * 获取状态机id
     *
     * @return 状态机id
     */
    @Override
    public String getMachineId() {
        return this.machineId;
    }

    /**
     * 设置准备状态
     *
     * @param ready 是否准备完成
     */
    @Override
    public void setReady(boolean ready) {
        this.ready = ready;
    }


    /**
     * 发送事件
     *
     * @param sourceState 源状态
     * @param event       事件
     * @param ctx         上下文
     * @return 目标状态
     */
    @Override
    public S fireEvent(S sourceState, E event, C ctx) {
        this.checkReady();
        State<S, E, C> state = this.getState(sourceState);
        return this.doTransition(state, event, ctx).getId();
    }

    /**
     * 生成流程图
     *
     * @return 流程图
     */
    @Override
    public String generateUML() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.machineId).append(" start").append('\n');
        for(State state: stateMap.values()){
            sb.append(state.generateUML());
        }
        sb.append(this.machineId).append(" end");
        return sb.toString();
    }

    /**
     * 状态流转
     *
     * @param sourceState 源状态
     * @param event       事件
     * @param ctx         用户上下文
     * @return 目标状态
     */
    private State<S, E, C> doTransition(State<S, E, C> sourceState, E event, C ctx) {
        Optional<Transition<S, E, C>> transition = sourceState.getTransition(event);
        if (transition.isPresent()) {
            return transition.get().transit(ctx);
        }
        return sourceState;
    }

    /**
     * 获取状态
     *
     * @param stateId 状态id
     * @return 状态
     */
    private State<S, E, C> getState(S stateId) {
        State<S, E, C> state = StateHelper.getState(stateMap, stateId);
        if (state == null) {
            System.out.println(this.generateUML());
            throw new StateMachineException(stateId + " is not found, please check state machine");
        }
        return state;
    }

    /**
     * 校验状态机是否已经初始化完成
     */
    private void checkReady() {
        if (!ready) {
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }
}
