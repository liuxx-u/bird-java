package com.bird.statemachine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author liuxx
 * @since 2020/8/6
 */
public class State<S,E,C> {

    private final S stateId;
    private HashMap<E, Transition<S, E, C>> transitions = new HashMap<>();

    State(S stateId) {
        this.stateId = stateId;
    }

    /**
     * 获取状态id
     *
     * @return id
     */
    S getId() {
        return this.stateId;
    }

    public Transition<S, E, C> setTransition(E event, Transition<S,E,C> transition) {
        verify(event, transition);
        transitions.put(event, transition);
        return transition;
    }

    public Optional<Transition<S, E, C>> getTransition(E event) {
        return Optional.ofNullable(transitions.get(event));
    }

    String generateUML(){
        StringBuilder sb = new StringBuilder();
        for(Transition transition: this.getTransitions()){
            sb.append(transition.getSource().getId())
                    .append(" : ")
                    .append(transition.getEvent())
                    .append('\n');
        }
        return sb.toString();
    }

    private Collection<Transition<S, E, C>> getTransitions() {
        return transitions.values();
    }

    private void verify(E event, Transition<S,E,C> newTransition) {
        Transition existingTransition = transitions.get(event);
        if(existingTransition != null){
            if(existingTransition.equals(newTransition)){
                throw new StateMachineException(existingTransition+" already Exist, you can not add another one");
            }
        }
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof State) {
            State other = (State) anObject;
            return this.stateId.equals(other.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return stateId.toString();
    }
}
