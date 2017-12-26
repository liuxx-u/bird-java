package com.bird.service.zero;

import com.bird.core.service.AbstractService;
import com.bird.service.zero.dto.PermissionDTO;
import com.bird.service.zero.model.Permission;

public interface PermissionService extends AbstractService<Permission> {
    /**
     * 获取权限信息
     * @return
     */
    PermissionDTO getPermission(Long id);

    /**
     * 保存权限
     * @param dto
     */
    void savePermission(PermissionDTO dto);
}
