package com.bird.service.zero.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bird.core.Check;
import com.bird.core.NameValue;
import com.bird.core.service.AbstractServiceImpl;
import com.bird.core.utils.ListHelper;
import com.bird.service.zero.RoleService;
import com.bird.service.zero.dto.RolePermissionDTO;
import com.bird.service.zero.mapper.RoleMapper;
import com.bird.service.zero.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
@Service
@CacheConfig(cacheNames = "zero_role")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.RoleService")
public class RoleServiceImpl extends AbstractServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 获取角色的权限id集合
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        Check.GreaterThan(roleId, 0L, "roleId");
        return roleMapper.getRolePermissionIds(roleId);
    }

    /**
     * 设置角色权限
     * @param dto 角色权限信息
     */
    @Override
    @Transactional
    public void setRolePermissions(RolePermissionDTO dto) {
        Check.NotNull(dto,"dto");
        Check.GreaterThan(dto.getRoleId(),0L,"roleId");

        roleMapper.deleteRolePermissions(dto.getRoleId());
        roleMapper.setRolePermissions(dto);
    }

    /**
     * 获取所有角色扼要信息
     *
     * @return
     */
    @Override
    public List<NameValue> getAllRoleBriefs() {
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        wrapper.where("delFlag = 0");
        List<Role> roles = mapper.selectList(wrapper);

        List<NameValue> result = ListHelper.init(roles)
                .select(role -> new NameValue(role.getName(), role.getId().toString()));
        return result;
    }
}
