package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_form")
public class FormDTO extends EntityDTO {
    private String name;
    private String key;
    private String description;
    private Boolean enabled;
    private Boolean withTab;
    private String saveUrl;
    private String tabType;
    private String tabPosition;
    private String defaultGroupName;
    private Integer lineCapacity;
}
