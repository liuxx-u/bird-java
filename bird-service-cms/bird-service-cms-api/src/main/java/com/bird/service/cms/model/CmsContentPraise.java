package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractFullModel;

@TableName("cms_content_praise")
public class CmsContentPraise extends AbstractFullModel {
    private Long userId;
    private Long contentId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
}
