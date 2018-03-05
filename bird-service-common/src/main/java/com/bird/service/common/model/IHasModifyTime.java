package com.bird.service.common.model;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/3/5
 *
 * Model-最后修改时间接口
 */
public interface IHasModifyTime {
    Date getModifiedTime();

    void setModifiedTime(Date modifiedTime);
}
