package com.bird.statemachine;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liuxx
 * @since 2020/8/6
 */
@Getter
@Setter
public class Transition<S,E,C> {

    private State<S, E, C> source;

    private E event;

    private List<Action<S, C>> actions = new ArrayList<>();

    public Transition(State<S, E, C> source, E event) {
        this.source = source;
        this.event = event;
    }

    public void addAction(Action<S, C> action) {
        if (action == null) {
            return;
        }

        this.actions.add(action);
        Collections.sort(this.actions);
    }

    public S transit(C ctx) {
        this.verify();
        for (Action<S, C> action : actions) {
            if (action.getCondition().apply(ctx)) {
                return action.getHandler().apply(ctx);
            }
        }

        return source.getId();
    }

    private void verify() {
        if (actions == null || actions.size() == 0) {
            throw new StateMachineException(String.format("event '%s' actions is empty.", event));
        }
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() + "]->";
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof Transition) {
            Transition other = (Transition) anObject;
            return this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.actions.equals(other.getActions());
        }
        return false;
    }
}
