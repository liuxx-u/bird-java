package com.bird.service.scheduler;

import com.bird.core.exception.UserFriendlyException;
import com.bird.service.common.service.query.PagedListQueryDTO;
import com.bird.service.common.service.query.PagedListResultDTO;
import com.bird.service.scheduler.dto.JobDTO;
import org.springframework.transaction.annotation.Transactional;

public interface SchedulerService {

    /**
     * 分页查询
     * @param query
     * @return
     */
    PagedListResultDTO queryPagedList(PagedListQueryDTO query);


    /**
     * 新增 定时任务
     * @param job
     */
    @Transactional
    void insertJob(JobDTO job) throws UserFriendlyException;

    /**
     * 修改 定时任务
     * @param job
     */
    @Transactional
    void updateJob(JobDTO job);

    /**
     * 删除 定时任务
     */
    void deleteJob(String jobName);
}
