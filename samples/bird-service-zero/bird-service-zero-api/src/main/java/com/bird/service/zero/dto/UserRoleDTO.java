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
public class UserRoleDTO extends AbstractDTO {
    private Long userId;
    private List<Long> roleIds;

    public UserRoleDTO(){
        roleIds = new ArrayList<>();
    }
}
