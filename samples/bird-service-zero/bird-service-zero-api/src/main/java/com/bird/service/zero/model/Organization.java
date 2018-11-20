package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuxx on 2017/11/1.
 */
@Getter
@Setter
@TableName("zero_organization")
public class Organization extends AbstractModel {
    private String name;
    private Long parentId;
    private String parentIds;
    private Integer orderNo;
    private String remark;
}
