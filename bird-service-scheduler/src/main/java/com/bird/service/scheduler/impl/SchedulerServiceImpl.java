package com.bird.service.scheduler.impl;

import com.bird.core.Check;
import com.bird.core.exception.UserFriendlyException;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.scheduler.QuartzManager;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.scheduler.SchedulerService;
import com.bird.service.scheduler.dto.JobDTO;
import com.bird.service.scheduler.mapper.SchedulerLogMapper;
import com.bird.service.scheduler.mapper.SchedulerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.scheduler.SchedulerService")
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private SchedulerMapper mapper;

    @Autowired
    private SchedulerLogMapper logMapper;

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    @Override
    public PagedListResultDTO queryPagedList(PagedListQueryDTO query) {

        String from = "`qrtz_triggers`,`qrtz_cron_triggers`,`qrtz_job_details`";
        String where = "`qrtz_triggers`.`TRIGGER_NAME`= `qrtz_cron_triggers`.`TRIGGER_NAME` AND `qrtz_triggers`.`JOB_NAME`=`qrtz_job_details`.`JOB_NAME`";

        PagedQueryParam param = new PagedQueryParam(query, JobDTO.class, "", from, where);
        param.setFrom(from);
        param.setWhere(where);

        Long totalCount = mapper.queryTotalCount(param);
        List<Map> items = new ArrayList<>();
        if (totalCount > 0) {
            items = mapper.queryPagedList(param);
        }
        return new PagedListResultDTO(totalCount, items);
    }

    /**
     * 新增 定时任务
     *
     * @param job
     */
    @Override
    public void insertJob(JobDTO job) {
        Check.NotNull(job, "job");
        Check.NotEmpty(job.getJobName(), "jobName");
        Check.NotEmpty(job.getGroupName(), "groupName");
        Check.NotEmpty(job.getJobClassName(), "jobClassName");
        Check.NotEmpty(job.getCronExpression(), "cronExpression");

        try {
                if(mapper.isExist(job.getJobName())){
                throw new UserFriendlyException("该任务已存在");
            }

            Class jobClass = Class.forName(job.getJobClassName());
            QuartzManager.addJob(job.getJobName(), job.getGroupName(), job.getJobName() + "_trigger", job.getGroupName(), jobClass, job.getCronExpression(), job.getDescription());
        } catch (ClassNotFoundException ex) {
            throw new UserFriendlyException("指定的类名不存在");
        }
    }

    /**
     * 修改 定时任务
     *
     * @param job
     */
    @Override
    public void updateJob(JobDTO job) {
        Check.NotNull(job, "job");
        Check.NotEmpty(job.getJobName(), "jobName");
        Check.NotEmpty(job.getGroupName(), "groupName");
        Check.NotEmpty(job.getCronExpression(), "cronExpression");

        try {
            QuartzManager.modifyJobTime(job.getJobName()+"_trigger", job.getGroupName(),job.getCronExpression());
        }catch (Exception ex){
            throw new UserFriendlyException("修改定时任务失败");
        }
    }

    /**
     * 删除 定时任务
     */
    @Override
    public void deleteJob(String jobName) {
        Check.NotEmpty(jobName,"jobName");
        mapper.removeJob(jobName);
    }
}
