package com.bird.service.common.model;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2019/7/11
 */
public interface IHasCreatorId<TKey extends Serializable> {

    /**
     * 获取创建者id
     *
     * @return id
     */
    TKey getCreatorId();
}
