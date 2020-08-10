package com.bird.statemachine.test;

import com.bird.statemachine.StateMachine;
import com.bird.statemachine.StateMachineFactory;
import com.bird.statemachine.builder.StateMachineBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author liuxx
 * @since 2020/8/7
 */
public class StateMachineTest {

    static String MACHINE_ID = "TestStateMachine";

    enum States {
        STATE1, STATE2, STATE3, STATE4
    }

    enum Events {
        EVENT1, EVENT2, EVENT3, EVENT4, INTERNAL_EVENT
    }

    static class Context {
        String operator = "frank";
        String entityId = "123465";
    }

    static class SubContext extends Context{
        String text = "subContext";
    }

    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilder.init();
        builder.externalTransition()
                .from(States.STATE1)
                .on(Events.EVENT1)
                .perform(ctx -> States.STATE2);

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID);
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new SubContext());
        Assert.assertEquals(States.STATE2, target);
    }

    @Test
    public void testExternalTransitionsNormal(){
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilder.init();
        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .on(Events.EVENT1)
                .perform(ctx -> States.STATE4);

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID+"1");
        States target = stateMachine.fireEvent(States.STATE2, Events.EVENT1, new Context());
        Assert.assertEquals(States.STATE4, target);
    }

    @Test
    public void testExternalInternalNormal(){
        StateMachine<States, Events, Context> stateMachine = buildStateMachine("testExternalInternalNormal");

        Context context = new Context();
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, context);
        Assert.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.INTERNAL_EVENT, context);
        Assert.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.EVENT2, context);
        Assert.assertEquals(States.STATE1, target);
        target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, context);
        Assert.assertEquals(States.STATE3, target);
    }

    private StateMachine<States, Events, Context> buildStateMachine(String machineId) {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilder.init();
        builder.externalTransition()
                .from(States.STATE1)
                .on(Events.EVENT1)
                .perform(ctx->States.STATE2);

        builder.internalTransition()
                .from(States.STATE2)
                .on(Events.INTERNAL_EVENT)
                .perform(ctx->States.STATE2);

        builder.externalTransition()
                .from(States.STATE2)
                .on(Events.EVENT2)
                .perform(ctx->States.STATE2);

        builder.externalTransition()
                .from(States.STATE1)
                .on(Events.EVENT3)
                .perform(ctx->States.STATE3);

        builder.build(machineId);

        StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get(machineId);
        System.out.println(stateMachine.generateUML());
        return stateMachine;
    }

    @Test
    public void testMultiThread(){
        buildStateMachine("testMultiThread");

        for(int i=0 ; i<10 ; i++){
            Thread thread = new Thread(()->{
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
                Assert.assertEquals(States.STATE2, target);
            });
            thread.start();
        }


        for(int i=0 ; i<10 ; i++) {
            Thread thread = new Thread(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT4, new Context());
                Assert.assertEquals(States.STATE4, target);
            });
            thread.start();
        }

        for(int i=0 ; i<10 ; i++) {
            Thread thread = new Thread(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, new Context());
                Assert.assertEquals(States.STATE3, target);
            });
            thread.start();
        }

    }
}
