package com.bird.scheduler;

import com.bird.core.scheduler.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuxx on 2017/8/22.
 */
public class TestJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("testjob执行");
    }
}
