package com.bird.service.common.service;

import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.model.IPO;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public abstract class AbstractStringService<M extends AbstractMapper<T>,T extends IPO<String>> extends AbstractService<M,T,String> {

    @Override
    protected boolean isEmptyKey(String id) {
        return StringUtils.isBlank(id);
    }
}
