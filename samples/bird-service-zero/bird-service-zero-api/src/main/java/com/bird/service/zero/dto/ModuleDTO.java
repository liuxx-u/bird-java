package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_module")
public class ModuleDTO extends EntityDTO {
    private String name;
    private Integer parentId;
    private String description;
    private Integer siteId;
    private Integer orderNo;
}
