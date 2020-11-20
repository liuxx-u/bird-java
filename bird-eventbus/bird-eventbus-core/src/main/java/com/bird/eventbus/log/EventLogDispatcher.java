package com.bird.eventbus.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Slf4j
public class EventLogDispatcher implements IEventLogDispatcher {

    private final IEventSendLogStore sendLogStore;
    private final IEventHandleLogStore handleLogStore;

    private final EventLogProperties logProperties;
    private EventLogStoreConsumer logStoreConsumer = new EventLogStoreConsumer();
    /**
     * 事件日志队列
     */
    private BlockingQueue<AbstractEventLog> logQueue;

    public EventLogDispatcher(IEventSendLogStore sendLogStore, IEventHandleLogStore eventHandlerStore, EventLogProperties logProperties) {
        this.sendLogStore = sendLogStore;
        this.handleLogStore = eventHandlerStore;
        this.logProperties = logProperties;

        this.logQueue = new LinkedBlockingQueue<>(logProperties.getDefaultQueueSize());
    }

    public void init() {
        if (sendLogStore instanceof NullEventLogStore && handleLogStore instanceof NullEventLogStore) {
            return;
        }

        ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern(this.logProperties.getThreadNamePattern())
                .daemon(this.logProperties.getDaemon())
                .build();

        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(this.logProperties.getPoolSize(), threadFactory);
        poolExecutor.scheduleAtFixedRate(logStoreConsumer, this.logProperties.getPeriod(), this.logProperties.getPeriod(), TimeUnit.SECONDS);

        // JVM退出时再执行一次保存操作
        Runtime.getRuntime().addShutdownHook(new Thread(logStoreConsumer));
    }

    /**
     * 发送事件日志
     *
     * @param eventLog 事件日志
     */
    @Override
    public void dispatch(AbstractEventLog eventLog) {
        if (eventLog == null) {
            return;
        }
        if (sendLogStore instanceof NullEventLogStore && handleLogStore instanceof NullEventLogStore) {
            return;
        }

        logQueue.add(eventLog);
        if (logQueue.size() >= this.logProperties.getThreshold()) {
            this.logStoreConsumer.run();
        }
    }

    /**
     * 事件处理结果存储结果消费者
     */
    private class EventLogStoreConsumer implements Runnable {
        @Override
        public void run() {
            List<AbstractEventLog> logs = new ArrayList<>();
            logQueue.drainTo(logs);

            if (CollectionUtils.isEmpty(logs)) {
                return;
            }

            List<EventSendLog> sendLogs = new ArrayList<>();
            List<EventHandleLog> handleLogs = new ArrayList<>();
            for (AbstractEventLog log : logs) {
                if (log instanceof EventSendLog) {
                    sendLogs.add((EventSendLog) log);
                } else if (log instanceof EventHandleLog) {
                    handleLogs.add((EventHandleLog) log);
                }
            }

            try {
                if (sendLogStore != null && !(sendLogStore instanceof NullEventLogStore) && !CollectionUtils.isEmpty(sendLogs)) {
                    sendLogStore.storeSendLogs(sendLogs);
                }
                if (handleLogStore != null && !(handleLogStore instanceof NullEventLogStore) && !CollectionUtils.isEmpty(handleLogs)) {
                    handleLogStore.storeHandleLogs(handleLogs);
                }
            } catch (Exception ex) {
                log.error("保存事件日志失败：" + ex.getMessage());
            }
        }
    }
}
