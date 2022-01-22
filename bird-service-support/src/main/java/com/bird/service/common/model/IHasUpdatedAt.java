package com.bird.service.common.model;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/3/5
 *
 * Model-最后修改时间接口
 */
public interface IHasUpdatedAt {

    /**
     * 获取修改时间
     * @return updatedAt
     */
    Date getUpdatedAt();

    /**
     * 设置修改时间
     * @param updatedAt updatedAt
     */
    void setUpdatedAt(Date updatedAt);
}
