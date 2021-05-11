package com.bird.statemachine.test;

import com.bird.statemachine.builder.StateMachineBuilder;
import com.bird.statemachine.factory.StateMachine;
import com.bird.statemachine.factory.StateMachineFactory;
import com.bird.statemachine.test.pojo.EventEnum;
import com.bird.statemachine.test.pojo.StateEnum;
import com.bird.statemachine.test.pojo.TestStateContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liuxx
 * @since 2021/5/11
 */
public class StateMachineTest {

    static String MACHINE_ID = "TestStateMachine";

    @Test
    public void testNormal() {
        StateMachineBuilder<StateEnum, EventEnum, TestStateContext> builder = StateMachineBuilder.init();
        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT1)
                .perform(1, ctx -> ctx.getAge() > 200, ctx -> StateEnum.STATE2)
                .perform(3, ctx -> ctx.getAge() > 50, ctx -> StateEnum.STATE3)
                .perform(2, ctx -> ctx.getAge() > 30, ctx -> StateEnum.STATE4);

        StateMachine<StateEnum, EventEnum, TestStateContext> stateMachine = builder.build(MACHINE_ID);
        StateEnum target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT1, new TestStateContext(60));
        Assert.assertEquals(StateEnum.STATE4, target);
    }

    @Test
    public void testConditionProcessorNormal() {
        StateMachineBuilder<StateEnum, EventEnum, TestStateContext> builder = StateMachineBuilder.init();
        builder.states()
                .fromAmong(StateEnum.STATE1, StateEnum.STATE2, StateEnum.STATE3)
                .on(EventEnum.EVENT1)
                .perform(1, ctx -> ctx.getAge() > 200, ctx -> StateEnum.STATE2)
                .perform(3, ctx -> ctx.getAge() > 50, ctx -> StateEnum.STATE3)
                .perform(2, ctx -> ctx.getAge() > 30, ctx -> StateEnum.STATE4);

        StateMachine<StateEnum, EventEnum, TestStateContext> stateMachine = builder.build(MACHINE_ID + "1");
        StateEnum target1 = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT1, new TestStateContext(60));
        Assert.assertEquals(StateEnum.STATE4, target1);

        StateEnum target2 = stateMachine.fireEvent(StateEnum.STATE2, EventEnum.EVENT1, new TestStateContext(60));
        Assert.assertEquals(StateEnum.STATE4, target2);

        StateEnum target3 = stateMachine.fireEvent(StateEnum.STATE3, EventEnum.EVENT1, new TestStateContext(60));
        Assert.assertEquals(StateEnum.STATE4, target3);
    }

    @Test
    public void testMixedProcessorNormal() {
        StateMachine<StateEnum, EventEnum, TestStateContext> stateMachine = buildStateMachine("testExternalInternalNormal");

        TestStateContext context = new TestStateContext(60);
        StateEnum target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT1, context);
        Assert.assertEquals(StateEnum.STATE4, target);
        target = stateMachine.fireEvent(StateEnum.STATE2, EventEnum.EVENT2, context);
        Assert.assertEquals(StateEnum.STATE2, target);

        context.setSex("female");
        target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
        Assert.assertEquals(StateEnum.STATE5, target);

        context.setSex("male");
        target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
        Assert.assertEquals(StateEnum.STATE3, target);
    }

    private StateMachine<StateEnum, EventEnum, TestStateContext> buildStateMachine(String machineId) {
        StateMachineBuilder<StateEnum, EventEnum, TestStateContext> builder = StateMachineBuilder.init();
        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT1)
                .perform(1, ctx -> ctx.getAge() > 200, ctx -> StateEnum.STATE2)
                .perform(3, ctx -> ctx.getAge() > 50, ctx -> StateEnum.STATE3)
                .perform(2, ctx -> ctx.getAge() > 30, ctx -> StateEnum.STATE4);

        builder.state()
                .from(StateEnum.STATE2)
                .on(EventEnum.EVENT2)
                .perform(ctx -> StateEnum.STATE2);

        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT3)
                .perform("male", ctx -> {
                    System.out.println("threadName:" + Thread.currentThread().getName() + ", male do it");
                    return StateEnum.STATE3;
                })
                .perform("female", ctx -> {
                    System.out.println("threadName:" + Thread.currentThread().getName() + ", female do it");
                    return StateEnum.STATE5;
                });

        builder.build(machineId);

        StateMachine<StateEnum, EventEnum, TestStateContext> stateMachine = StateMachineFactory.get(machineId);
        return stateMachine;
    }

    @Test
    public void testMultiThread() throws InterruptedException {
        buildStateMachine("testMultiThread");
        CountDownLatch cdh = new CountDownLatch(20);

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
                } catch (InterruptedException ignore) {
                }
                StateMachine<StateEnum, EventEnum, TestStateContext> stateMachine = StateMachineFactory.get("testMultiThread");
                TestStateContext context = new TestStateContext(60);

                StateEnum target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT1, context);
                Assert.assertEquals(StateEnum.STATE4, target);
                System.out.println("case 1 :" + target.getName() + ",threadName:" + Thread.currentThread().getName());

                context.setSex("female");
                target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
                Assert.assertEquals(StateEnum.STATE5, target);

                context.setSex("male");
                target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
                Assert.assertEquals(StateEnum.STATE3, target);
                cdh.countDown();
            });
            thread.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500));
                } catch (InterruptedException ignore) {
                }
                StateMachine<StateEnum, EventEnum, TestStateContext> stateMachine = StateMachineFactory.get("testMultiThread");
                TestStateContext context = new TestStateContext(60);

                StateEnum target = stateMachine.fireEvent(StateEnum.STATE2, EventEnum.EVENT2, context);
                Assert.assertEquals(StateEnum.STATE2, target);
                System.out.println("case 2 :" + target.getName() + ",threadName:" + Thread.currentThread().getName());

                context.setSex("female");
                target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
                Assert.assertEquals(StateEnum.STATE5, target);

                context.setSex("male");
                target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
                Assert.assertEquals(StateEnum.STATE3, target);
                cdh.countDown();
            });
            thread.start();
        }
        cdh.await();
    }
}
