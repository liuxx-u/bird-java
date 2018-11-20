package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_permission")
public class PermissionDTO extends EntityDTO {

    private String key;
    private String name;
    private Integer parentId;

    @TableField(exist = false)
    private String parentKey;
    private String remark;
}
