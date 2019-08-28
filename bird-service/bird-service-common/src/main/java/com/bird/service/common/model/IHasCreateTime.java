package com.bird.service.common.model;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/3/5
 *
 * Model-创建时间接口
 */
public interface IHasCreateTime {

    /**
     * 获取创建时间
     * @return
     */
    Date getCreateTime();

    /**
     * 设置创建时间
     * @param createTime
     */
    void setCreateTime(Date createTime);
}
