package com.bird.web.admin.controller.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bird.core.Check;
import com.bird.core.controller.OperationResult;
import com.bird.service.common.mapper.PagedQueryParam;
import com.bird.service.common.service.query.PagedListQueryDTO;
import com.bird.service.common.service.query.PagedListResultDTO;
import com.bird.service.scheduler.SchedulerLogService;
import com.bird.service.scheduler.SchedulerService;
import com.bird.service.scheduler.dto.JobDTO;
import com.bird.service.scheduler.dto.SchedulerLogDTO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "系统-定时任务接口")
@RestController
@RequestMapping("/sys/scheduler")
public class SchedulerController {

    @Reference
    private SchedulerService schedulerService;

    @Reference
    private SchedulerLogService logService;

    @PostMapping(value = "/getPaged")
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {

        PagedListResultDTO result = schedulerService.queryPagedList(query);

        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/insert")
    public OperationResult insert(@RequestBody JobDTO job) {
        schedulerService.insertJob(job);

        return OperationResult.Success("保存成功",null);
    }

    @PostMapping(value = "/update")
    public OperationResult update(@RequestBody JobDTO job) {
        schedulerService.updateJob(job);

        return OperationResult.Success("保存成功",null);
    }

    @PostMapping(value="/delete")
    public OperationResult delete(String id){
        schedulerService.deleteJob(id);

        return OperationResult.Success("删除成功",null);
    }

    @PostMapping(value = "/getPagedLog")
    public OperationResult<PagedListResultDTO> getPagedLog(@RequestBody PagedListQueryDTO query) {

        PagedQueryParam param = new PagedQueryParam(query, SchedulerLogDTO.class);
        PagedListResultDTO result = logService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/deleteLog")
    public OperationResult deleteLog(Long id){
        Check.GreaterThan(id,0L,"id");
        logService.softDelete(id);

        return OperationResult.Success("删除成功",null);
    }
}
