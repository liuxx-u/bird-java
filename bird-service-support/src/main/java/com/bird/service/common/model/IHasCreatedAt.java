package com.bird.service.common.model;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/3/5
 *
 * Model-创建时间接口
 */
public interface IHasCreatedAt {

    /**
     * 获取创建时间
     *
     * @return createdAt
     */
    Date getCreatedAt();

    /**
     * 设置创建时间
     *
     * @param createdAt createdAt
     */
    void setCreatedAt(Date createdAt);
}
