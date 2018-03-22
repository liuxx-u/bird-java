package com.bird.service.zero.impl;

import com.bird.core.Check;
import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.DicTypeService;
import com.bird.service.zero.dto.DicDTO;
import com.bird.service.zero.dto.DicTypeDTO;
import com.bird.service.zero.mapper.DicTypeMapper;
import com.bird.service.zero.model.DicType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;


/**
 * Created by liuxx on 2017/11/3.
 */
@Service
@CacheConfig(cacheNames = "zero_dicType")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.DicTypeService")
public class DicTypeServiceImpl extends AbstractService<DicTypeMapper,DicType> implements DicTypeService {

    /**
     * 根据key获取字典信息
     *
     * @return
     */
    @Override
    public DicDTO getDicByKey(String key) {
        return mapper.getDicByKey(key);
    }

    /**
     * 获取字典信息
     *
     * @param id
     * @return
     */
    @Override
    public DicTypeDTO getDicType(Long id) {
        Check.GreaterThan(id, 0L, "id");
        DicType dicType = queryById(id);
        DicTypeDTO result = dozer.map(dicType, DicTypeDTO.class);

        if (dicType.getParentId() != null && dicType.getParentId() > 0) {
            DicType parent = queryById(dicType.getParentId());
            if (parent != null) {
                result.setParentKey(parent.getKey());
            }
        }

        return result;
    }
}
