package com.bird.service.common.model;

import java.io.Serializable;

/**
 * Model - 租户id接口
 *
 * @author liuxx
 * @since 2020/5/20
 */
public interface IHasTenantId<TKey extends Serializable>  {

    /**
     * 获取租户id
     *
     * @return id
     */
    TKey getTenantId();

    /**
     * 设置租户id
     */
    void setTenantId();
}
