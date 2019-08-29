package com.bird.service.common.service;

import com.bird.core.utils.DozerHelper;
import com.bird.core.utils.NumberHelper;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public abstract class GenericLongService<M extends AbstractMapper<T>,T extends IModel<Long>> extends GenericService<M,T,Long> implements IGenericLongService<T> {


    @Autowired
    protected DozerHelper dozer;

    @Override
    protected Boolean isEmptyKey(Long id) {
        return NumberHelper.isNotPositive(id);
    }
}
