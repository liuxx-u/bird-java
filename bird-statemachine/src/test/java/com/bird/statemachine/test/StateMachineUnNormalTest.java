package com.bird.statemachine.test;

import com.bird.statemachine.builder.StateMachineBuilder;
import com.bird.statemachine.exception.StateMachineException;
import com.bird.statemachine.test.pojo.EventEnum;
import com.bird.statemachine.test.pojo.StateEnum;
import com.bird.statemachine.test.pojo.TestStateContext;
import org.junit.Test;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class StateMachineUnNormalTest {

    @Test(expected = StateMachineException.class)
    public void testDuplicatedEvent() {
        StateMachineBuilder<StateEnum, EventEnum, TestStateContext> builder = StateMachineBuilder.init();
        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT1)
                .perform(ctx->StateEnum.STATE2);

        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT1)
                .perform(ctx->StateEnum.STATE2);
    }

    @Test(expected = StateMachineException.class)
    public void testDuplicateMachine() {
        StateMachineBuilder<StateEnum, EventEnum, TestStateContext> builder = StateMachineBuilder.init();
        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT1)
                .perform(ctx->StateEnum.STATE2);

        builder.build("DuplicatedMachine");
        builder.build("DuplicatedMachine");
    }
}
