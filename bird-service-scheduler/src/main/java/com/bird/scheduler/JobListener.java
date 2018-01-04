package com.bird.scheduler;

import com.bird.service.scheduler.mapper.SchedulerLogMapper;
import com.bird.service.scheduler.model.SchedulerLog;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by liuxx on 2017/8/22.
 */
public class JobListener implements org.quartz.JobListener {

    @Autowired
    private SchedulerLogMapper logMapper;

    public String getName() {
        return "bird-quartz-scheduler-listener";
    }

    public void jobExecutionVetoed(JobExecutionContext context) {
    }

    // 任务开始前
    public void jobToBeExecuted(final JobExecutionContext context) {
    }

    // 任务结束后
    public void jobWasExecuted(final JobExecutionContext context, JobExecutionException exp) {
        JobDetail jobDetail = context.getJobDetail();
        if(jobDetail instanceof JobDetailImpl){
            JobDetailImpl job = (JobDetailImpl)jobDetail;

            SchedulerLog log = new SchedulerLog();
            log.setJobName(job.getName());
            log.setGroupName(job.getGroup());

            log.setTriggerName(context.getTrigger().getKey().getName());
            log.setFireTime(context.getFireTime());
            log.setScheduledFireTime(context.getScheduledFireTime());
            log.setRefireCount(context.getRefireCount());
            log.setPreviousFireTime(context.getPreviousFireTime());
            log.setNextFireTime(context.getNextFireTime());
            log.setCreateTime(new Date());
            logMapper.insert(log);
        }
    }
}
