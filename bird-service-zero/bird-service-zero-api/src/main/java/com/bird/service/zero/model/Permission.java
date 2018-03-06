package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractFullModel;

@TableName("zero_permission")
public class Permission extends AbstractFullModel{
    private String key;
    private String name;
    private Long parentId;
    private String remark;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
}
