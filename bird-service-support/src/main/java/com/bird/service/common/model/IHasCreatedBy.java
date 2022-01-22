package com.bird.service.common.model;


/**
 * @author liuxx
 * @date 2019/7/11
 */
public interface IHasCreatedBy {

    /**
     * 获取创建者
     *
     * @return 创建者
     */
    String getCreatedBy();

    /**
     * 设置创建者
     * @param createdBy 创建者
     */
    void setCreatedBy(String createdBy);
}
