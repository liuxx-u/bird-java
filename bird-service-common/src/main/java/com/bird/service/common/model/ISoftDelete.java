package com.bird.service.common.model;

/**
 * @author liuxx
 * @date 2018/2/28
 *
 * Model-软删除接口
 */
public interface ISoftDelete {
    Boolean getDelFlag();

    void setDelFlag(Boolean delFlag);
}
