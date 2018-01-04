package com.bird.scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created by liuxx on 2017/8/14.
 */
public abstract class AbstractJob implements Job {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public void execute(JobExecutionContext context) throws JobExecutionException {
        long start = System.currentTimeMillis();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String targetObject = jobDataMap.getString("targetObject");
        String targetMethod = jobDataMap.getString("targetMethod");
        logger.info("定时任务开始执行: [{}.{}]", targetObject, targetMethod);
        try {
            ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
            Object refer = applicationContext.getBean(targetObject);
            refer.getClass().getDeclaredMethod(targetMethod).invoke(refer);
            double time = (System.currentTimeMillis() - start) / 1000.0;
            logger.info("定时任务[{}.{}]用时：{}s", targetObject, targetMethod, time);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
