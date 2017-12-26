package com.bird.service.zero.impl;

import com.bird.core.Check;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.service.AbstractServiceImpl;
import com.bird.core.service.TreeDTO;
import com.bird.core.utils.DozerHelper;
import com.bird.service.zero.DicTypeService;
import com.bird.service.zero.dto.DicDTO;
import com.bird.service.zero.dto.DicTypeDTO;
import com.bird.service.zero.dto.PermissionDTO;
import com.bird.service.zero.mapper.DicTypeMapper;
import com.bird.service.zero.model.DicType;
import com.bird.service.zero.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxx on 2017/11/3.
 */
@Service
@CacheConfig(cacheNames = "zero_dicType")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.DicTypeService")
public class DicTypeServiceImpl extends AbstractServiceImpl<DicType> implements DicTypeService {
    @Autowired
    private DicTypeMapper dicTypeMapper;

    @Autowired
    private DozerHelper dozerHelper;

    /**
     * 根据key获取字典信息
     *
     * @return
     */
    @Override
    public DicDTO getDicByKey(String key) {
        return dicTypeMapper.getDicByKey(key);
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
        DicTypeDTO result = dozerHelper.map(dicType, DicTypeDTO.class);

        if (dicType.getParentId() != null && dicType.getParentId() > 0) {
            DicType parent = queryById(dicType.getParentId());
            if (parent != null) {
                result.setParentKey(parent.getKey());
            }
        }

        return result;
    }
}
