package com.bird.service.common.service;

import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.model.StringPureModel;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public abstract class GenericStringService<M extends AbstractMapper<T>,T extends StringPureModel> extends GenericService<M,T,String> implements IGenericStringService<T> {


    @Override
    protected Boolean isEmptyKey(String id) {
        return StringUtils.isBlank(id);
    }
}
