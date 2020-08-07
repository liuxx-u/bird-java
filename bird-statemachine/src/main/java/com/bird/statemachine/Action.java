package com.bird.statemachine;

/**
 * @author liuxx
 * @since 2020/8/6
 */
public interface Action<S, E, C> {

    /**
     * 执行方法
     * @param from 源状态
     * @param to 目标状态
     * @param event 事件
     * @param context 用户上下文
     */
    void execute(S from, S to, E event, C context);
}
