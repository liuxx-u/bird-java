package com.cczcrv.scheduler;

import com.cczcrv.core.scheduler.job.AbstractJob;
import com.cczcrv.service.cms.ContentService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuxx on 2017/8/22.
 */
public class TestJob extends AbstractJob {
    @Autowired
    private ContentService contentService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("testjob执行");
    }
}
