package com.bird.service.common.model;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2019/7/11
 */
public interface IHasModifierId<TKey extends Serializable> {

    /**
     * 获取修改者id
     *
     * @return id
     */
    TKey getModifierId();
}
