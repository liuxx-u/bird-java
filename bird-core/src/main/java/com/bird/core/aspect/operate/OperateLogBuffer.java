package com.bird.core.aspect.operate;

import com.bird.core.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author liuxx
 * @date 2019/1/15
 */
@Slf4j
public class OperateLogBuffer {
    private BufferConsumer consumer = new BufferConsumer();
    private static BlockingQueue<OperateLogInfo> logQueue = new LinkedBlockingQueue<>();

    public OperateLogBuffer() {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(2, (new BasicThreadFactory.Builder()).build());
        poolExecutor.scheduleAtFixedRate(consumer, 0, 20, TimeUnit.SECONDS);
    }

    public void enqueue(OperateLogInfo logInfo) {
        logQueue.add(logInfo);

        if (logQueue.size() >= 100) {
            consumer.run();
        }
    }

    public class BufferConsumer implements Runnable {

        @Override
        public void run() {

            List<OperateLogInfo> logs = new ArrayList<>();
            logQueue.drainTo(logs);

            if (CollectionUtils.isEmpty(logs)) return;

            try {
                IOperateLogStore store = SpringContextHolder.getBean(IOperateLogStore.class);
                store.store(logs);
            } catch (Exception ex) {
                log.error("保存操作日志失败：" + ex.getMessage());
            }
        }
    }
}
