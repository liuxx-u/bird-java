package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cms_content")
public class CmsContent extends AbstractModel {
    private String title;
    private Long classifyId;
    private String brief;
    private String content;
    private String link;
    private String cover;
    private Integer orderNo;
    private Integer browserNo;
    private Integer praiseNo;
}
