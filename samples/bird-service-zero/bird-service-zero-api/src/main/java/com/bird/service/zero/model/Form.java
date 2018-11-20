package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_form")
public class Form extends AbstractModel {
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
