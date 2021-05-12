package com.bird.statemachine.initialize.annotation;

import com.bird.statemachine.StateProcessor;
import com.bird.statemachine.builder.StateMachineBuilder;
import com.bird.statemachine.initialize.StateMachineInitializer;
import com.bird.statemachine.initialize.StateProcessorDefinition;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/5/11
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class AnnotationStateMachineInitializer implements StateMachineInitializer {

    private final List<StateProcessor> processors;

    public AnnotationStateMachineInitializer(List<StateProcessor> processors) {
        this.processors = processors;
    }

    @Override
    @PostConstruct
    public void initialize() {
        Map<String, List<StateProcessorDefinition>> machineProcessorMap = this.parseProcessorDefinitions();

        for (Map.Entry<String, List<StateProcessorDefinition>> entry : machineProcessorMap.entrySet()) {
            String machineId = entry.getKey();
            List<StateProcessorDefinition> processorDefinitions = entry.getValue();

            StateMachineBuilder builder = StateMachineBuilder.init();
            for (StateProcessorDefinition definition : processorDefinitions) {
                builder.state()
                        .from(definition.getState())
                        .on(definition.getEvent())
                        .perform(definition.getSceneId(), definition.getStateProcessor());
            }
            builder.build(machineId);
        }
    }


    private Map<String, List<StateProcessorDefinition>> parseProcessorDefinitions() {

        Map<String, List<StateProcessorDefinition>> machineProcessorMap = new HashMap<>(8);
        for (StateProcessor processor : processors) {
            StateHandler stateHandler = processor.getClass().getAnnotation(StateHandler.class);
            if (stateHandler == null) {
                continue;
            }

            StateProcessorDefinition definition = new StateProcessorDefinition(stateHandler, processor);
            List<StateProcessorDefinition> processorDefinitions = machineProcessorMap.computeIfAbsent(stateHandler.machineId(), p -> new ArrayList<>());
            processorDefinitions.add(definition);
        }
        return machineProcessorMap;
    }
}
