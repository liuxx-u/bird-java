package com.bird.statemachine;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

/**
 * @author liuxx
 * @since 2020/8/6
 */
@Getter
@Setter
public class Action<S, C> implements Comparable<Action<S,C>> {
    public static int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    public static int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * 匹配优先级
     */
    private int priority;
    /**
     * 匹配条件
     */
    private Function<C, Boolean> condition;
    /**
     * 执行方法
     */
    private Function<C, S> handler;

    public Action() {
    }

    public Action(int priority, Function<C, Boolean> condition, Function<C, S> handler) {
        this.priority = priority;
        this.condition = condition;
        this.handler = handler;
    }


    @Override
    public int compareTo(Action<S, C> obj) {
        if (obj == null) {
            return 1;
        }
        return Integer.compare(this.priority, obj.priority);
    }
}
