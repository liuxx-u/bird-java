package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liuxx on 2017/11/2.
 */
@Getter
@Setter
@TableName("zero_dicItem")
public class DicItem extends AbstractModel {
    private String name;
    private String code;
    private Long dicTypeId;
    private Integer orderNo;
    private Boolean disable;
}
