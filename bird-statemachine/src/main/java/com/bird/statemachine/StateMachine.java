package com.bird.statemachine;

/**
 * @author liuxx
 * @since 2020/8/6
 */
public interface StateMachine<S,E,C> {

    /**
     * 获取状态机id
     * @return 状态机id
     */
    String getMachineId();

    /**
     * 设置准备状态
     *
     * @param ready 准备状态
     */
    void setReady(boolean ready);

    /**
     * 发送事件
     *
     * @param sourceState 源状态
     * @param event 事件
     * @param ctx 上下文
     * @return 目标状态
     */
    S fireEvent(S sourceState, E event, C ctx);

    /**
     * 生成流程图
     * @return 流程图
     */
    String generateUML();
}
