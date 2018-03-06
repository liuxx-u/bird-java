package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractFullModel;

/**
 * Created by liuxx on 2017/11/1.
 */
@TableName("zero_organization")
public class Organization extends AbstractFullModel {
    private String name;
    private Long parentId;
    private String parentIds;
    private Integer orderNo;
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
