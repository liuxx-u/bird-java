package com.bird.service.scheduler.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;

import java.util.Date;

@TableName("scheduler_log")
public class SchedulerLog extends AbstractModel {
    private String jobName;
    private String groupName;
    private String triggerName;
    private Date fireTime;
    private int refireCount;
    private Date scheduledFireTime;
    private Date previousFireTime;
    private Date nextFireTime;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public int getRefireCount() {
        return refireCount;
    }

    public void setRefireCount(int refireCount) {
        this.refireCount = refireCount;
    }

    public Date getScheduledFireTime() {
        return scheduledFireTime;
    }

    public void setScheduledFireTime(Date scheduledFireTime) {
        this.scheduledFireTime = scheduledFireTime;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }
}
