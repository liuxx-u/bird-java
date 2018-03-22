package com.bird.service.zero.impl;

import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.FieldService;
import com.bird.service.zero.mapper.FieldMapper;
import com.bird.service.zero.model.Field;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "zero_form_field")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.FieldService")
public class FieldServiceImpl extends AbstractService<FieldMapper,Field> implements FieldService {
}
