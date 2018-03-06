package com.bird.service.scheduler.impl;

import com.bird.service.common.service.AbstractServiceImpl;
import com.bird.service.scheduler.SchedulerLogService;
import com.bird.service.scheduler.model.SchedulerLog;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "scheduler_log")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.scheduler.SchedulerLogService")
public class SchedulerLogServiceImpl extends AbstractServiceImpl<SchedulerLog> implements SchedulerLogService {
}
