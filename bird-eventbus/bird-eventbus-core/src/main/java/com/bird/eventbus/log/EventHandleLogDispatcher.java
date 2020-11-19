package com.bird.eventbus.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Slf4j
public class EventHandleLogDispatcher {

    private final IEventHandleLogStore handlerStore;

    public EventHandleLogDispatcher(IEventHandleLogStore eventHandlerStore) {
        this.handlerStore = eventHandlerStore;
    }
    /**
     * 消费结果队列
     */
    private BlockingQueue<EventHandleLog> handleLogQueue = new LinkedBlockingQueue<>();

    @PostConstruct
    public void initHandlerStoreThread() {
        if (handlerStore != null) {
            ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(2, (new BasicThreadFactory.Builder()).build());
            poolExecutor.scheduleAtFixedRate(new EventHandleLogDispatcher.EventHandleStoreConsumer(), 0, 10, TimeUnit.SECONDS);
        }
    }

    /**
     * 事件处理结果存储结果消费者
     */
    private class EventHandleStoreConsumer implements Runnable {
        @Override
        public void run() {
            List<EventHandleLog> results = new ArrayList<>();
            handleLogQueue.drainTo(results);

            if (CollectionUtils.isEmpty(results)) {
                return;
            }

            try {
                if (handlerStore != null) {
                    handlerStore.store(results);
                }
            } catch (Exception ex) {
                log.error("保存EventBus消费结果失败：" + ex.getMessage());
            }
        }
    }
}
