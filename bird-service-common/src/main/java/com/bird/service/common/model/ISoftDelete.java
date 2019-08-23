package com.bird.service.common.model;

/**
 * @author liuxx
 * @date 2018/2/28
 *
 * Model-软删除接口
 */
public interface ISoftDelete {

    /**
     * 获取是否软删除
     * @return delFlag
     */
    Boolean getDelFlag();

    /**
     * 设置为删除
     * @param delFlag delFlag
     */
    void setDelFlag(Boolean delFlag);
}
