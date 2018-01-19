package com.bird.scheduler.job;

import com.bird.scheduler.AbstractJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("testjob执行");
    }
}
