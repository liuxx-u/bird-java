package com.bird.service.common.service;

import com.bird.service.common.model.IModel;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public interface IGenericLongService<T extends IModel<Long>> extends IGenericService<T,Long> {
}
