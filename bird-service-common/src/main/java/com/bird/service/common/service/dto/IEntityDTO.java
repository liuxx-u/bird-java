package com.bird.service.common.service.dto;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2019/8/26
 */
public interface IEntityDTO<TKey extends Serializable> extends Serializable {

    /**
     * 获取主键
     * @return id
     */
    TKey getId();

    /**
     * 设置主键
     * @param id id
     */
    void setId(TKey id);
}
