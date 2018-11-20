package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_form_field")
public class FieldDTO extends EntityDTO {
    private String name;
    private String key;
    private String defaultValue;
    private String tips;
    private String groupName;
    private String validateRegular;
    private Boolean isRequired;
    private Integer orderNo;
    private String fieldType;
    private Long formId;
    private String optionsKey;
}
