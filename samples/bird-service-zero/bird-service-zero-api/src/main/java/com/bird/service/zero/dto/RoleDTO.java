package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuxx on 2017/10/27.
 */
@Getter
@Setter
@TableName("zero_role")
public class RoleDTO extends EntityDTO {
    private String name;
    private String remark;
    private Long organizationId;
}
