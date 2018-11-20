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
@TableName("zero_dicType")
public class DicType extends AbstractModel {
    private String name;
    private String key;
    private Long parentId;
    private String placeholder;
    private String remark;
    private String defaultCode;
}
