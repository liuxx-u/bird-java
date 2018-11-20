package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cms_content_attribute")
public class CmsContentAttribute extends AbstractModel {
    private Long contentId;
    private Long attributeId;
    private String value;
    private String optionIds;
}
