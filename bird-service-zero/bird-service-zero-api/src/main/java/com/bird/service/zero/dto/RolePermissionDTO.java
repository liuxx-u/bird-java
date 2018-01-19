package com.bird.service.zero.dto;

import com.bird.core.service.AbstractDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
public class RolePermissionDTO extends AbstractDTO {
    private Long roleId;
    private List<Long> permissionIds;

    public RolePermissionDTO(){
        permissionIds = new ArrayList<>();
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
