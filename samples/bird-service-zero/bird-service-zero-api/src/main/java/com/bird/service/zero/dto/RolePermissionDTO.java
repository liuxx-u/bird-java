package com.bird.service.zero.dto;

import com.bird.service.common.service.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/10/27.
 */
@Getter
@Setter
public class RolePermissionDTO extends AbstractDTO {
    private Long roleId;
    private List<Long> permissionIds;

    public RolePermissionDTO(){
        permissionIds = new ArrayList<>();
    }
}
