package com.bird.service.zero.dto;

import com.bird.core.service.AbstractDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
public class UserRoleDTO extends AbstractDTO {
    private Long userId;
    private List<Long> roleIds;

    public UserRoleDTO(){
        roleIds = new ArrayList<>();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
