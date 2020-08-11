package com.bird.statemachine.test;

import com.bird.statemachine.StateMachineException;
import com.bird.statemachine.builder.StateMachineBuilder;
import org.junit.Test;

/**
 * StateMachineUnNormalTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 5:52 PM
 */
public class StateMachineUnNormalTest {

    @Test(expected = StateMachineException.class)
    public void testDuplicatedTransition() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilder.init();
        builder.transition()
                .from(StateMachineTest.States.STATE1)
                .on(StateMachineTest.Events.EVENT1)
                .perform(ctx->StateMachineTest.States.STATE2);

        builder.transition()
                .from(StateMachineTest.States.STATE1)
                .on(StateMachineTest.Events.EVENT1)
                .perform(ctx->StateMachineTest.States.STATE2);
    }

    @Test(expected = StateMachineException.class)
    public void testDuplicateMachine() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilder.init();
        builder.transition()
                .from(StateMachineTest.States.STATE1)
                .on(StateMachineTest.Events.EVENT1)
                .perform(ctx->StateMachineTest.States.STATE2);

        builder.build("DuplicatedMachine");
        builder.build("DuplicatedMachine");
    }
}
