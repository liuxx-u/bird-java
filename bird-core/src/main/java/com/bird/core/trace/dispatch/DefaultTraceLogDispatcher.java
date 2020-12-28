package com.bird.core.trace.dispatch;

import com.bird.core.trace.TraceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author liuxx
 * @since 2020/10/9
 */
@Slf4j
public class DefaultTraceLogDispatcher implements ITraceLogDispatcher {

    private final static int DEFAULT_QUEUE_SIZE = 1024;

    private final ITraceLogStore traceLogStore;
    private final DefaultTraceDispatcherProperties dispatcherProperties;
    private final DefaultTraceLogStoreConsumer traceLogStoreConsumer = new DefaultTraceLogStoreConsumer();

    public DefaultTraceLogDispatcher(ITraceLogStore traceLogStore, DefaultTraceDispatcherProperties dispatcherProperties) {
        this.traceLogStore = traceLogStore;
        this.dispatcherProperties = dispatcherProperties;
    }

    public void init() {
        ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern(this.dispatcherProperties.getThreadNamePattern())
                .daemon(this.dispatcherProperties.getDaemon())
                .build();

        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(this.dispatcherProperties.getPoolSize(), threadFactory);
        poolExecutor.scheduleAtFixedRate(traceLogStoreConsumer, this.dispatcherProperties.getPeriod(), this.dispatcherProperties.getPeriod(), TimeUnit.SECONDS);

        // JVM退出时再执行一次保存操作
        Runtime.getRuntime().addShutdownHook(new Thread(traceLogStoreConsumer));
    }

    /**
     * 消费结果队列
     */
    private BlockingQueue<TraceDefinition> traceQueue = new LinkedBlockingQueue<>(DEFAULT_QUEUE_SIZE);

    /**
     * 发送跟踪日志
     *
     * @param traceLogs 跟踪日志
     */
    @Override
    public void dispatch(Collection<TraceDefinition> traceLogs) {
        if (CollectionUtils.isEmpty(traceLogs)) {
            return;
        }

        traceQueue.addAll(traceLogs);

        if (traceQueue.size() >= this.dispatcherProperties.getThreshold()) {
            this.traceLogStoreConsumer.run();
        }
    }

    /**
     * 跟踪日志存储消费者
     */
    private class DefaultTraceLogStoreConsumer implements Runnable {
        @Override
        public void run() {
            List<TraceDefinition> traceLogs = new ArrayList<>();
            traceQueue.drainTo(traceLogs);

            if (CollectionUtils.isEmpty(traceLogs)) {
                return;
            }

            try {
                if (traceLogStore != null) {
                    traceLogStore.store(traceLogs);
                }
            } catch (Exception ex) {
                log.error("跟踪日志保存失败.", ex);
            }
        }
    }
}
