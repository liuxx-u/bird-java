package com.bird.service.common.mapper.record;

import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author shaojie
 */
@Slf4j
public class RecordExchanger implements Runnable{

    private static final int DEFAULT_SIZE = 1024;
    private static final int DEFAULT_INTERVAL = 5000;

    private static final Queue<TableFieldRecord> MPSC_QUEUE = PlatformDependent.newMpscQueue(DEFAULT_SIZE);


    /** 记录周期, 单位: 毫秒数 */
    private long interval;
    private boolean enabled;
    private TableFieldRecorder recorder;
    private Thread thread;

    public RecordExchanger(long interval, boolean enabled, TableFieldRecorder recorder) {
        this.interval = interval <= 0 ? DEFAULT_INTERVAL : interval;
        this.enabled = enabled;
        this.recorder = recorder;
        if(enabled){
            // 如果没启用. 线程都不用开
            initThread();
        }
    }

    private void initThread() {
        // 创建线程, 并作为后台线程运行
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public static void offer(TableFieldRecord record){
        // 放入队列, 如果放入失败不报错
        if(!MPSC_QUEUE.offer(record)){
            log.warn("offer record failed. it mostly happened when the queue is full. current record info : {}", record);
        }
    }

    public static TableFieldRecord take(){
        // 从队列中取, 如果取不到不报错
        return MPSC_QUEUE.poll();
    }

    @Override
    public void run() {
        while(enabled){
            record();
            // current doesn't have any update record, wait
            try {
                // 先获取当前对象的锁, 只有获取了锁之后, 才可以进行等待或唤醒操作
                synchronized (this){
                    // 每次记录完之后, 等待一个周期
                    wait(interval);
                }
            } catch (InterruptedException e) {
                // 恢复打断状态
                Thread.interrupted();
                log.error(e.getMessage(),e);
            }
        }
    }

    public void record(){
        TableFieldRecord record;
        // 一直读, 知道读完为止
        List<TableFieldRecord> records = new ArrayList<>();
        while((record = take()) != null){
            records.add(record);
        }
        recorder.record(records);
    }

}
