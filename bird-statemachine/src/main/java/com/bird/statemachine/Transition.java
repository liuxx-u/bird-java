package com.bird.statemachine;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author liuxx
 * @since 2020/8/6
 */
@Getter
@Setter
public class Transition<S,E,C> {

    private State<S, E, C> source;

    private E event;

    private PriorityQueue<Action<S, C>> actions = new PriorityQueue<>(Comparator.comparing(Action::getPriority));

    public Transition() {
    }

    public Transition(State<S, E, C> source, E event) {
        this.source = source;
        this.event = event;
    }

    public void addAction(Action<S, C> action) {
        if (action == null) {
            return;
        }

        this.actions.add(action);
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
