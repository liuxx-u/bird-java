package com.bird.service.common.model;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/3/5
 *
 * Model-最后修改时间接口
 */
public interface IHasModifyTime {

    /**
     * 获取修改时间
     * @return modifiedTime
     */
    Date getModifiedTime();

    /**
     * 设置修改时间
     * @param modifiedTime modifiedTime
     */
    void setModifiedTime(Date modifiedTime);
}
