package com.bird.service.zero.mapper;

import com.bird.core.mapper.AbstractMapper;
import com.bird.service.zero.dto.RolePermissionDTO;
import com.bird.service.zero.model.Role;
import com.bird.service.zero.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
public interface RoleMapper extends AbstractMapper<Role> {
    /**
     * 获取角色的权限集合
     * @param roleId 角色Id
     * @return
     */
    List<Long> getRolePermissionIds(@Param("roleId") Long roleId);

    /**
     * 设置角色权限信息
     * @param dto 角色权限信息
     */
    void setRolePermissions(@Param("dto") RolePermissionDTO dto);

    /**
     * 删除角色的所有权限
     * @param roleId 删除角色权限信息
     */
    void deleteRolePermissions(@Param("roleId") Long roleId);
}
