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
        StateMachineBuilder<TestStateContext> builder = StateMachineBuilder.init();
        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT1)
                .perform(1, ctx -> ctx.getAge() > 200, ctx -> StateEnum.STATE2.getName())
                .perform(3, ctx -> ctx.getAge() > 50, ctx -> StateEnum.STATE3.getName())
                .perform(2, ctx -> ctx.getAge() > 30, ctx -> StateEnum.STATE4.getName());

        StateMachine<TestStateContext> stateMachine = builder.build(MACHINE_ID);
        String target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT1, new TestStateContext(60));
        Assert.assertEquals(StateEnum.STATE4.getName(), target);
    }

    @Test
    public void testConditionProcessorNormal() {
        StateMachineBuilder<TestStateContext> builder = StateMachineBuilder.init();
        builder.states()
                .fromAmong(StateEnum.STATE1, StateEnum.STATE2, StateEnum.STATE3)
                .on(EventEnum.EVENT1)
                .perform(1, ctx -> ctx.getAge() > 200, ctx -> StateEnum.STATE2.getName())
                .perform(3, ctx -> ctx.getAge() > 50, ctx -> StateEnum.STATE3.getName())
                .perform(2, ctx -> ctx.getAge() > 30, ctx -> StateEnum.STATE4.getName());

        StateMachine<TestStateContext> stateMachine = builder.build(MACHINE_ID + "1");
        String target1 = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT1, new TestStateContext(60));
        Assert.assertEquals(StateEnum.STATE4.getName(), target1);

        String target2 = stateMachine.fireEvent(StateEnum.STATE2, EventEnum.EVENT1, new TestStateContext(60));
        Assert.assertEquals(StateEnum.STATE4.getName(), target2);

        String target3 = stateMachine.fireEvent(StateEnum.STATE3, EventEnum.EVENT1, new TestStateContext(60));
        Assert.assertEquals(StateEnum.STATE4.getName(), target3);
    }

    @Test
    public void testMixedProcessorNormal() {
        StateMachine<TestStateContext> stateMachine = buildStateMachine("testExternalInternalNormal");

        TestStateContext context = new TestStateContext(60);
        String target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT1, context);
        Assert.assertEquals(StateEnum.STATE4.getName(), target);
        target = stateMachine.fireEvent(StateEnum.STATE2, EventEnum.EVENT2, context);
        Assert.assertEquals(StateEnum.STATE2.getName(), target);

        context.setSex("female");
        target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
        Assert.assertEquals(StateEnum.STATE5.getName(), target);

        context.setSex("male");
        target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
        Assert.assertEquals(StateEnum.STATE3.getName(), target);
    }

    private StateMachine<TestStateContext> buildStateMachine(String machineId) {
        StateMachineBuilder<TestStateContext> builder = StateMachineBuilder.init();
        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT1)
                .perform(1, ctx -> ctx.getAge() > 200, ctx -> StateEnum.STATE2.getName())
                .perform(3, ctx -> ctx.getAge() > 50, ctx -> StateEnum.STATE3.getName())
                .perform(2, ctx -> ctx.getAge() > 30, ctx -> StateEnum.STATE4.getName());

        builder.state()
                .from(StateEnum.STATE2)
                .on(EventEnum.EVENT2)
                .perform(ctx -> StateEnum.STATE2.getName());

        builder.state()
                .from(StateEnum.STATE1)
                .on(EventEnum.EVENT3)
                .perform("male", ctx -> {
                    System.out.println("threadName:" + Thread.currentThread().getName() + ", male do it");
                    return StateEnum.STATE3.getName();
                })
                .perform("female", ctx -> {
                    System.out.println("threadName:" + Thread.currentThread().getName() + ", female do it");
                    return StateEnum.STATE5.getName();
                });

        builder.build(machineId);

        StateMachine<TestStateContext> stateMachine = StateMachineFactory.get(machineId);
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
                StateMachine<TestStateContext> stateMachine = StateMachineFactory.get("testMultiThread");
                TestStateContext context = new TestStateContext(60);

                String target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT1, context);
                Assert.assertEquals(StateEnum.STATE4.getName(), target);
                System.out.println("case 1 :" + target + ",threadName:" + Thread.currentThread().getName());

                context.setSex("female");
                target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
                Assert.assertEquals(StateEnum.STATE5.getName(), target);

                context.setSex("male");
                target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
                Assert.assertEquals(StateEnum.STATE3.getName(), target);
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
                StateMachine<TestStateContext> stateMachine = StateMachineFactory.get("testMultiThread");
                TestStateContext context = new TestStateContext(60);

                String target = stateMachine.fireEvent(StateEnum.STATE2, EventEnum.EVENT2, context);
                Assert.assertEquals(StateEnum.STATE2.getName(), target);
                System.out.println("case 2 :" + target + ",threadName:" + Thread.currentThread().getName());

                context.setSex("female");
                target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
                Assert.assertEquals(StateEnum.STATE5.getName(), target);

                context.setSex("male");
                target = stateMachine.fireEvent(StateEnum.STATE1, EventEnum.EVENT3, context);
                Assert.assertEquals(StateEnum.STATE3.getName(), target);
                cdh.countDown();
            });
            thread.start();
        }
        cdh.await();
    }
}
