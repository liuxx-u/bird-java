package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cms_content_praise")
public class CmsContentPraise extends AbstractModel {
    private Long userId;
    private Long contentId;
}
