package com.bird.service.cms.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;

@TableName("cms_classify")
public class CmsClassifyDTO extends EntityDTO {
    @TableField("`cms_classify`.`name`")
    private String name;
    private Long parentId;
    @TableField("`cms_classify`.`parentIds`")
    private String parentIds;
    private Integer orderNo;
    private String logo;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
