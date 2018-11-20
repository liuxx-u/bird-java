package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuxx on 2017/10/27.
 */
@Getter
@Setter
@TableName("zero_role")
public class Role extends AbstractModel {
    private String name;
    private String remark;
    private Long organizationId;
}
