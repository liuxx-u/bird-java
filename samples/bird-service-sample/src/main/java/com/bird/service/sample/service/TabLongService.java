package com.bird.service.sample.service;

import com.bird.service.common.datasource.TargetDataSource;
import com.bird.service.common.service.GenericLongService;
import com.bird.service.sample.model.TabLong;
import com.bird.service.sample.service.mapper.TabLongMapper;
import org.springframework.stereotype.Service;

/**
 * @author liuxx
 * @date 2019/8/29
 */
@Service
@TargetDataSource("default")
public class TabLongService extends GenericLongService<TabLongMapper, TabLong> {
}
