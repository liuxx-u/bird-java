package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.service.EntityDTO;

@TableName("zero_site")
public class SiteDTO extends EntityDTO {
    private String name;
    private String key;
    @TableField("`zero_site`.`host`")
    private String host;
    private String indexUrl;
    private String loginNotifyUrl;
    private String remark;
    private Boolean disabled;
    private String permissionName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getLoginNotifyUrl() {
        return loginNotifyUrl;
    }

    public void setLoginNotifyUrl(String loginNotifyUrl) {
        this.loginNotifyUrl = loginNotifyUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
