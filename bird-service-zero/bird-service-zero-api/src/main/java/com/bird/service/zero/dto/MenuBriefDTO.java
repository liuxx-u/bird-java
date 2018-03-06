package com.bird.service.zero.dto;


import com.bird.service.common.service.dto.EntityDTO;

/**
 * Created by liuxx on 2017/10/31.
 */
public class MenuBriefDTO extends EntityDTO {
    private String name;
    private String url;
    private String icon;
    private String parentId;
    private String permissionName;

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

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
