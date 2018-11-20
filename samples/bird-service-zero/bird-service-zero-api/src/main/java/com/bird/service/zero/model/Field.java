package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_form_field")
public class Field extends AbstractModel {
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
