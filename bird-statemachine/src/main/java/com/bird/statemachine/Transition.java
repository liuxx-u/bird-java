package com.bird.statemachine;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @since 2020/8/6
 */
@Getter
@Setter
public class Transition<S,E,C> {

    private State<S, E, C> source;

    private State<S, E, C> target;

    private E event;

    private Condition<C> condition;

    private Action<S, E, C> action;

    private TransitionType transitionType = TransitionType.EXTERNAL;

    public State<S, E, C> transit(C ctx) {
        this.verify();
        if (condition == null || condition.isSatisfied(ctx)) {
            if (action != null) {
                action.execute(source.getId(), target.getId(), event, ctx);
            }
            return target;
        }

        return source;
    }

    private void verify() {
        if (transitionType == TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() + ", " + transitionType + "]->" + target;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof Transition) {
            Transition other = (Transition) anObject;
            return this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget());
        }
        return false;
    }
}
