package com.bird.service.common.model;

import java.util.Date;

/**
 * @author liuxx
 * @date 2018/2/27
 */
public abstract class AbstractFullModel extends AbstractModel<Long> implements ISoftDelete,IHasCreateTime,IHasModifyTime {
    private Boolean delFlag;
    private Date createTime;
    private Date modifiedTime;

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
