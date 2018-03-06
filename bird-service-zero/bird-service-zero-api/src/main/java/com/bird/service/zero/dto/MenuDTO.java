package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;

/**
 * Created by liuxx on 2017/10/30.
 */
@TableName("zero_menu")
public class MenuDTO extends EntityDTO {
    private String name;
    private String url;
    private String icon;
    private String parentId;
    private int orderNo;
    private String permissionName;
    private String remark;
    private int visitType;//'1.游客访问;2.登录访问;3.权限访问'

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public int getVisitType() {
        return visitType;
    }

    public void setVisitType(int visitType) {
        this.visitType = visitType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
