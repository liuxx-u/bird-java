package com.bird.service.common.service;

import com.bird.core.utils.NumberHelper;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.model.IPO;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public abstract class AbstractLongService<M extends AbstractMapper<T>,T extends IPO<Long>> extends AbstractService<M,T,Long> {


    @Override
    protected boolean isEmptyKey(Long id) {
        return NumberHelper.isNotPositive(id);
    }
}
