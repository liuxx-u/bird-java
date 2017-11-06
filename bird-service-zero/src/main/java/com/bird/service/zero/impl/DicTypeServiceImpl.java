package com.bird.service.zero.impl;

import com.bird.core.service.AbstractServiceImpl;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.DicTypeService;
import com.bird.service.zero.model.DicType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxx on 2017/11/3.
 */
@Service
@CacheConfig(cacheNames = "zero_dicType")
@com.alibaba.dubbo.config.annotation.Service
public class DicTypeServiceImpl extends AbstractServiceImpl<DicType> implements DicTypeService {
    /**
     * 获取字典类型 树形数据
     *
     * @return
     */
    @Override
    public List<TreeDTO> getDicTypeTreeData() {
        return null;
    }
}
