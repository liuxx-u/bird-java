package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractFullModel;

@TableName("cms_content_attribute")
public class CmsContentAttribute extends AbstractFullModel {
    private Long contentId;
    private Long attributeId;
    private String value;
    private String optionIds;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(String optionIds) {
        this.optionIds = optionIds;
    }
}
