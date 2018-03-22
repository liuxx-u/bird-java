package com.bird.service.zero.impl;

import com.bird.core.Check;
import com.bird.core.utils.DozerHelper;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.service.AbstractServiceImpl;
import com.bird.service.zero.PermissionService;
import com.bird.service.zero.dto.PermissionDTO;
import com.bird.service.zero.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "zero_permission")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.PermissionService")
public class PermissionServiceImpl extends AbstractServiceImpl<Permission> implements PermissionService {

    @Autowired
    private DozerHelper dozerHelper;

    /**
     * 获取权限信息
     *
     * @return
     */
    @Override
    public PermissionDTO getPermission(Long id) {
        Check.GreaterThan(id, 0L, "id");
        Permission permission = queryById(id);
        PermissionDTO result = dozerHelper.map(permission, PermissionDTO.class);

        if (permission.getParentId() != null && permission.getParentId() > 0) {
            Permission parentPermission = queryById(permission.getParentId());
            if (parentPermission != null) {
                result.setParentKey(parentPermission.getKey());
            }
        }

        return result;
    }

    /**
     * 保存权限信息
     *
     * @param dto
     */
    @Override
    public void savePermission(PermissionDTO dto) {
        Check.NotNull(dto,"dto");
        CommonSaveParam param = new CommonSaveParam(dto, PermissionDTO.class);
        save(param);
    }
}