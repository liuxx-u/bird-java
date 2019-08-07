package com.bird.trace.client.dispatch;

import com.bird.trace.client.TraceLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 * @date 2019/8/7
 */
@Slf4j
public class DefaultTraceLogDispatcher implements ITraceLogDispatcher {
    private final TraceLogQueueConsumer consumer = new TraceLogQueueConsumer();
    private final BlockingQueue<TraceLog> logQueue = new LinkedBlockingQueue<>();

    private final IDefaultTraceLogStore logStore;

    public DefaultTraceLogDispatcher(IDefaultTraceLogStore logStore){
        this.logStore = logStore;

        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(2, (new BasicThreadFactory.Builder()).build());
        poolExecutor.scheduleAtFixedRate(consumer, 0, 20, TimeUnit.SECONDS);
    }

    /**
     * 发送跟踪日志
     *
     * @param traceLogs 跟踪日志集合
     */
    @Override
    public void dispatch(Collection<TraceLog> traceLogs) {
        logQueue.addAll(traceLogs);

        if (logQueue.size() >= 100) {
            consumer.run();
        }
    }

    /**
     * 跟踪日志 消费线程
     */
    public class TraceLogQueueConsumer implements Runnable {

        @Override
        public void run() {

            List<TraceLog> logs = new ArrayList<>();
            logQueue.drainTo(logs);

            if (CollectionUtils.isEmpty(logs)) return;

            try {
                logStore.store(logs);
            } catch (Exception ex) {
                log.error("保存操作日志失败：" + ex.getMessage());
            }
        }
    }
}
