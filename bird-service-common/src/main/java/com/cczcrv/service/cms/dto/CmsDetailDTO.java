package com.bird.service.cms.dto;

import com.bird.service.cms.model.Content;

/**
 * Created by liuxx on 2017/6/30.
 */
public class CmsDetailDTO extends CmsResultDTO {
    private String content;

    public CmsDetailDTO() {
        super();
    }

    public CmsDetailDTO(Content content) {
        super(content);
        this.content = content.getContents();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
