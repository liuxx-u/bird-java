package com.bird.service.sample.service;

import com.bird.service.common.datasource.TargetDataSource;
import com.bird.service.common.service.GenericStringService;
import com.bird.service.common.service.dto.IEntityDTO;
import com.bird.service.sample.model.TabString;
import com.bird.service.sample.service.mapper.TabStringMapper;
import org.springframework.stereotype.Service;

/**
 * @author liuxx
 * @date 2019/8/29
 */
@Service
@TargetDataSource("db2")
public class TabStringService extends GenericStringService<TabStringMapper, TabString> {

    @Override
    //@TargetDataSource("db2")
    public String save(IEntityDTO<String> dto) {
        return super.save(dto);
    }
}
