package com.bird.service.cms.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cms_attribute")
public class CmsAttributeDTO extends EntityDTO {
    private String name;
    private String key;
    private String defaultValue;
    private String tips;
    private String groupName;
    private String validateRegular;
    private boolean isRequired;
    private Integer orderNo;
    private String fieldType;
    private Long classifyId;
    private String optionsKey;
}
