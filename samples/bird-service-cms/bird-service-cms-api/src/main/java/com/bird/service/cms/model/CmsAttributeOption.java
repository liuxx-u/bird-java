package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cms_attribute_option")
public class CmsAttributeOption extends AbstractModel {
    private String value;
    private Integer orderNo;
    private String remark;
    private Long attributeId;
}
