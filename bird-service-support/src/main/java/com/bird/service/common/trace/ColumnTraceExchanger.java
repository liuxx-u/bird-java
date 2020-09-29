package com.bird.service.common.trace;

import com.bird.service.common.trace.define.ColumnTraceDefinition;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shaojie
 */
@Slf4j
public class ColumnTraceExchanger {

    private static final int DEFAULT_SIZE = 1024;

    /**
     * 阈值模式: 到达阈值后, 触发
     */
    private static final int MODE_THRESHOLD = 1;
    /**
     * 周期模式: 指定周期后, 执行一次
     */
    private static final int MODE_PERIOD = 2;
    /**
     * 混合模式: 周期执行的同时, 如果指定周期内, 到达了阈值. 主动触发一次
     */
    public static final int MODE_MIXED = 3;

    private static final Queue<ColumnTraceDefinition> MPSC_QUEUE = PlatformDependent.newMpscQueue(DEFAULT_SIZE);

    private static Exchanger exchanger;

    /**
     * 是否启用
     */
    private volatile boolean enabled;

    private IColumnTraceRecorder recorder;

    public ColumnTraceExchanger(ColumnTraceProperties properties, IColumnTraceRecorder recorder) {
        this.enabled = properties.isEnabled();
        this.recorder = recorder;
        if (enabled) {
            // 如果没启用. 线程都不用开
            initExchanger(properties);
        }
    }

    private void initExchanger(ColumnTraceProperties properties) {
        int mode = properties.getMode();
        if (MODE_THRESHOLD == mode) {
            exchanger = new ThresholdExchanger(properties);
        } else if (MODE_PERIOD == mode) {
            exchanger = new PeriodExchanger(properties);
        } else {
            exchanger = new Exchanger(properties);
        }
        // 创建后台线程, 用于执行记录
        Thread thread = new Thread(exchanger, properties.getThreadName());
        thread.setDaemon(true);
        thread.start();
        // 因为我们创建的线程是不会关闭的, 所以我们需要在退出的时候通知线程结束
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.enabled = false));
    }

    public static void offer(ColumnTraceDefinition record) {
        // 放入队列, 如果放入失败不报错
        if (!MPSC_QUEUE.offer(record)) {
            log.warn("offer record failed. it mostly happened when the queue is full. current record info : {}", record);
        }
        // 放入之后, 记录一下
        if (exchanger != null) {
            exchanger.afterOffer(MPSC_QUEUE);
        }
    }

    public static ColumnTraceDefinition poll() {
        // 从队列中取, 如果取不到不报错
        return MPSC_QUEUE.poll();
    }

    public class Exchanger implements Runnable {

        protected int threshold;
        /*** 记录周期, 单位: 毫秒数         */
        private long interval;

        protected ReentrantLock lock = new ReentrantLock();
        protected Condition condition = lock.newCondition();

        public Exchanger(ColumnTraceProperties properties) {
            this.threshold = properties.getThreshold();
            this.interval = properties.getInterval();
        }

        public void afterOffer(Queue<ColumnTraceDefinition> queue) {
            if (queue.size() >= threshold) {
                // 尝试获取锁,如果获取成功,则唤醒. 如果获取失败,说明线程正在处理, 那就不通知了
                if (lock.tryLock()) {
                    // 线程唤醒
                    lock.notify();
                }
            }
        }

        @Override
        public void run() {
            while (enabled) {
                exchange();
            }
        }

        public void exchange() {
            record();
            // 先获取当前对象的锁, 只有获取了锁之后, 才可以进行等待或唤醒操作
            lock.lock();
            // current doesn't have any update record, wait
            try {
                // 每次记录完之后, 等待一个周期
                condition.await(interval, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // 恢复中断状态
                Thread.interrupted();
                log.error(e.getMessage(), e);
            }finally {
                lock.unlock();
            }
        }

    }

    public class ThresholdExchanger extends Exchanger {

        private ThresholdExchanger(ColumnTraceProperties properties) {
            super(properties);
        }

        @Override
        public void exchange() {
            lock.lock();
            try {
                condition.await();
                // 如果被通知了, 进行一次记录
                record();
            } catch (InterruptedException e) {
                // 恢复中断状态
                Thread.interrupted();
                log.error(e.getMessage(), e);
            } finally {
                lock.unlock();
            }
        }
    }

    public class PeriodExchanger extends Exchanger {

        public PeriodExchanger(ColumnTraceProperties properties) {
            super(properties);
        }

        @Override
        public void afterOffer(Queue<ColumnTraceDefinition> queue) {

        }

    }

    public void record() {
        ColumnTraceDefinition record;
        // 一直读, 知道读完为止
        List<ColumnTraceDefinition> records = new ArrayList<>();
        while ((record = poll()) != null) {
            records.add(record);
        }
        recorder.record(records);
    }

}
