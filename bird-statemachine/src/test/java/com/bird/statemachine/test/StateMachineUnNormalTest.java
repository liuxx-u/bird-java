package com.bird.statemachine.test;

import com.bird.statemachine.Action;
import com.bird.statemachine.Condition;
import com.bird.statemachine.StateMachine;
import com.bird.statemachine.StateMachineException;
import com.bird.statemachine.builder.StateMachineBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * StateMachineUnNormalTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 5:52 PM
 */
//public class StateMachineUnNormalTest {
//
//    @Test
//    public void testConditionNotMeet() {
//        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilder.init();
//        builder.transition()
//                .from(StateMachineTest.States.STATE1)
//                .to(StateMachineTest.States.STATE2)
//                .on(StateMachineTest.Events.EVENT1)
//                .when(checkConditionFalse())
//                .perform(doAction());
//
//        StateMachine<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> stateMachine = builder.build("NotMeetConditionMachine");
//        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new StateMachineTest.Context());
//        Assert.assertEquals(StateMachineTest.States.STATE1, target);
//    }
//
//
//    @Test(expected = StateMachineException.class)
//    public void testDuplicatedTransition() {
//        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilder.init();
//        builder.transition()
//                .from(StateMachineTest.States.STATE1)
//                .to(StateMachineTest.States.STATE2)
//                .on(StateMachineTest.Events.EVENT1)
//                .when(checkCondition())
//                .perform(doAction());
//
//        builder.transition()
//                .from(StateMachineTest.States.STATE1)
//                .to(StateMachineTest.States.STATE2)
//                .on(StateMachineTest.Events.EVENT1)
//                .when(checkCondition())
//                .perform(doAction());
//    }
//
//    @Test(expected = StateMachineException.class)
//    public void testDuplicateMachine() {
//        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilder.init();
//        builder.transition()
//                .from(StateMachineTest.States.STATE1)
//                .to(StateMachineTest.States.STATE2)
//                .on(StateMachineTest.Events.EVENT1)
//                .when(checkCondition())
//                .perform(doAction());
//
//        builder.build("DuplicatedMachine");
//        builder.build("DuplicatedMachine");
//    }
//
//    private Condition<StateMachineTest.Context> checkCondition() {
//        return (ctx) -> true;
//    }
//
//    private Condition<StateMachineTest.Context> checkConditionFalse() {
//        return (ctx) -> false;
//    }
//
//}
