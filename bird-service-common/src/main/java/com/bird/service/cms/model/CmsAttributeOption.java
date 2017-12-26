package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.model.AbstractModel;

@TableName("cms_attribute_option")
public class CmsAttributeOption extends AbstractModel {
    private String value;
    private Integer orderNo;
    private String remark;
    private Long attributeId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }
}
