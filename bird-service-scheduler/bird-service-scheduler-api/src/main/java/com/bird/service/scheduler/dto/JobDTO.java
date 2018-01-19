package com.bird.service.scheduler.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.bird.core.service.AbstractDTO;

import java.util.Date;

public class JobDTO extends AbstractDTO {
    @TableField("`qrtz_triggers`.`JOB_NAME`")
    private String jobName;
    @TableField("`qrtz_job_details`.`JOB_CLASS_NAME`")
    private String jobClassName;
    @TableField("`qrtz_cron_triggers`.`CRON_EXPRESSION`")
    private String cronExpression;
    @TableField("`qrtz_job_details`.`JOB_GROUP`")
    private String groupName;
    @TableField("`qrtz_job_details`.`DESCRIPTION`")
    private String description;
    @TableField("`qrtz_triggers`.`NEXT_FIRE_TIME`")
    private Date nextFireTime;
    @TableField("`qrtz_triggers`.`PREV_FIRE_TIME`")
    private Date prevFireTime;
    @TableField("`qrtz_triggers`.`PRIORITY`")
    private int priority;
    @TableField("`qrtz_triggers`.`TRIGGER_STATE`")
    private String state;
    @TableField("`qrtz_triggers`.`START_TIME`")
    private Date startTime;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(Date prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
