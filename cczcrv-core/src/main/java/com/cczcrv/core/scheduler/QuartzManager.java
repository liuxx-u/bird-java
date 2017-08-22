package com.cczcrv.core.scheduler;

/**
 * Created by liuxx on 2017/8/21.
 */

import com.cczcrv.core.scheduler.job.AbstractJob;
import org.quartz.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * Quartz调度管理器
 *
 * @author Administrator
 *
 */
public class QuartzManager {
    private static String JOB_GROUP_NAME = "CCZCRV_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "CCZCRV_TRIGGERGROUP_NAME";

    private static Scheduler scheduler;

    public void setScheduler(Scheduler scheduler) {
        QuartzManager.scheduler = scheduler;
    }

    /**
     * @param jobClass       任务
     * @param cronExpression 时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务，使用默认的任务名，任务组名，触发器名，触发器组名
     * @Title: QuartzManager.java
     */
    public static void addJob(Class<? extends AbstractJob> jobClass, String cronExpression) {
        String defaultKey = jobClass.getName();
        String jobName = defaultKey + "_job";
        String triggerName = defaultKey + "_trigger";

        addJob(jobName, JOB_GROUP_NAME, triggerName, TRIGGER_GROUP_NAME, jobClass, cronExpression);
    }

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cronExpression   时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     * @Title: QuartzManager.java
     */
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<? extends AbstractJob> jobClass, String cronExpression) {
        try {
            JobDetail jobDetail = newJob(jobClass).withIdentity(jobName, jobGroupName).build();// 任务名，任务组，任务执行类
            // 触发器
            Trigger trigger = newTrigger().withIdentity(triggerName, triggerGroupName).withSchedule(cronSchedule(cronExpression)).build();// 触发器名,触发器组
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobClass       任务
     * @param cronExpression 时间设置
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     * @Title: QuartzManager.java
     */
    public static void modifyJobTime(Class<? extends AbstractJob> jobClass, String cronExpression) {
        String triggerName = jobClass.getName() + "_trigger";
        modifyJobTime(triggerName, TRIGGER_GROUP_NAME, cronExpression);
    }

    /**
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器分组名
     * @param cronExpression   时间设置
     * @Description: 修改一个任务的触发时间
     * @Title: QuartzManager.java
     */
    public static void modifyJobTime(String triggerName, String triggerGroupName, String cronExpression) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cronExpression)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(cronSchedule(cronExpression));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobClass 任务
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     * @Title: QuartzManager.java
     */
    public static void removeJob(Class<? extends AbstractJob> jobClass) {
        String defaultKey = jobClass.getName();
        String jobName = defaultKey + "_job";
        String triggerName = defaultKey + "_trigger";

        removeJob(jobName, JOB_GROUP_NAME, triggerName, TRIGGER_GROUP_NAME);
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     * @Title: QuartzManager.java
     */
    public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器

            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     * @Title: QuartzManager.java
     */
    public static void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     * @Title: QuartzManager.java
     */
    public static void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }
}
