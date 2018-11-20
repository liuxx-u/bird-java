package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("zero_permission")
public class Permission extends AbstractModel{
    private String key;
    private String name;
    private Long parentId;
    private String remark;
}
