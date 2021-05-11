package com.bird.statemachine.condition;

import com.bird.statemachine.State;
import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author liuxx
 * @since 2021/5/7
 */
public class ConditionalStateProcessorCluster<S extends State,C extends StateContext> implements StateProcessor<S,C> {

    private final List<ConditionalStateProcessor<S, C>> processors;

    public ConditionalStateProcessorCluster(){
        this.processors = new ArrayList<>();
    }

    /**
     * add conditional state processor
     * @param processor {@link ConditionalStateProcessor}
     */
    public void addCondition(ConditionalStateProcessor<S, C> processor){
        this.processors.add(processor);
        this.processors.sort(Comparator.comparingInt(ConditionalStateProcessor::getPriority));
    }

    @Override
    public S action(C context) {
        for (ConditionalStateProcessor<S, C> processor : processors) {
            if (processor.judge(context)) {
                return processor.action(context);
            }
        }
        return null;
    }
}
