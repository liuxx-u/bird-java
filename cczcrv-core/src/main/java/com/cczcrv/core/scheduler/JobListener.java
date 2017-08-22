package com.cczcrv.core.scheduler;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

/**
 * Created by liuxx on 2017/8/22.
 */
public class JobListener implements org.quartz.JobListener {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public String getName() {
        return "cczcrv-quartz-scheduler-listener";
    }

    public void jobExecutionVetoed(JobExecutionContext context) {
    }

    // 任务开始前
    public void jobToBeExecuted(final JobExecutionContext context) {
    }

    // 任务结束后
    public void jobWasExecuted(final JobExecutionContext context, JobExecutionException exp) {
    }
}
