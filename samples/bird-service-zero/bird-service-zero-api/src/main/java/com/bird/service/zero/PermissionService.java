package com.bird.service.zero;

import com.bird.service.common.service.IService;
import com.bird.service.zero.dto.PermissionDTO;
import com.bird.service.zero.model.Permission;

public interface PermissionService extends IService<Permission> {
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
