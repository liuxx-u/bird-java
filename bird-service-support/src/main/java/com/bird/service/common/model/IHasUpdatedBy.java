package com.bird.service.common.model;

/**
 * @author liuxx
 * @date 2019/7/11
 */
public interface IHasUpdatedBy {

    /**
     * 获取修改者
     *
     * @return 修改者
     */
    String getUpdatedBy();

    /**
     * 设置修改者
     * @param updatedBy 修改者
     */
    void setUpdatedBy(String updatedBy);
}
