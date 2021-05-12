package com.bird.statemachine.condition;

import com.bird.statemachine.StateContext;
import com.bird.statemachine.StateProcessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author liuxx
 * @since 2021/5/7
 */
public class ConditionalStateProcessorCluster<C extends StateContext> implements StateProcessor<C> {

    private final List<ConditionalStateProcessor<C>> processors;

    public ConditionalStateProcessorCluster() {
        this.processors = new ArrayList<>();
    }

    /**
     * add conditional state processor
     *
     * @param processor {@link ConditionalStateProcessor}
     */
    public void addCondition(ConditionalStateProcessor<C> processor) {
        this.processors.add(processor);
        this.processors.sort(Comparator.comparingInt(ConditionalStateProcessor::getPriority));
    }

    @Override
    public String action(C context) {
        for (ConditionalStateProcessor<C> processor : processors) {
            if (processor.judge(context)) {
                return processor.action(context);
            }
        }
        return null;
    }
}
