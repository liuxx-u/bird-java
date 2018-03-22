package com.bird.service.zero;

import com.bird.core.NameValue;
import com.bird.service.common.service.IService;
import com.bird.service.zero.dto.RolePermissionDTO;
import com.bird.service.zero.model.Role;

import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取角色的权限id集合
     * @param roleId 角色id
     * @return
     */
    List<Long> getRolePermissionIds(Long roleId);

    /**
     * 设置角色权限
     * @param dto 角色权限信息
     */
    void setRolePermissions(RolePermissionDTO dto);

    /**
     * 获取所有角色扼要信息
     * @return
     */
    List<NameValue> getAllRoleBriefs();
}
