package com.bird.service.zero.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bird.core.NameValue;
import com.bird.core.service.AbstractServiceImpl;
import com.bird.core.utils.ListHelper;
import com.bird.service.zero.RoleService;
import com.bird.service.zero.dto.RolePermissionDTO;
import com.bird.service.zero.model.Role;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
@Service
@CacheConfig(cacheNames = "zero_role")
@com.alibaba.dubbo.config.annotation.Service
public class RoleServiceImpl extends AbstractServiceImpl<Role> implements RoleService {

    @Override
    public void setRolePermissions(RolePermissionDTO dto) {

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
