package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_module")
public class Module extends AbstractModel {
    private String name;
    private Integer parentId;
    private String description;
    private Integer siteId;
    private Integer orderNo;
}
