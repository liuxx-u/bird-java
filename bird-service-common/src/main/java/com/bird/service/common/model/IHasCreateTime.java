package com.bird.service.common.model;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/3/5
 *
 * Model-创建时间接口
 */
public interface IHasCreateTime {

    Date getCreateTime();

    void setCreateTime(Date createTime);
}
