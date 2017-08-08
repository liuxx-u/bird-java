package com.cczcrv.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cczcrv.core.model.AbstractModel;

/**
 * Created by liuxx on 2017/6/27.
 */
@TableName("Cms_ContentAttribute")
public class ContentAttributeMap extends AbstractModel {
    /// <summary>
    /// 产品Id
    /// </summary>
    @TableField("ContentId")
    private long contentId;


    /// <summary>
    /// 属性Id
    /// </summary>
    @TableField("AttributeId")
    private long attributeId;

    /// <summary>
    /// 属性值
    /// </summary>
    @TableField("Value")
    private String value;


    @TableField("AttributeOptionIds")
    private String attributeOptionIds;

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(long attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttributeOptionIds() {
        return attributeOptionIds;
    }

    public void setAttributeOptionIds(String attributeOptionIds) {
        this.attributeOptionIds = attributeOptionIds;
    }
}
