package com.bird.service.cms.dto;

import com.bird.service.common.service.dto.AbstractDTO;
import java.util.HashMap;
import java.util.Map;

public class CmsFullContentDTO extends AbstractDTO {
    private CmsContentDTO content;
    private Map<String,String> attribute;

    public CmsFullContentDTO(){
        attribute = new HashMap<>();
    }

    public CmsContentDTO getContent() {
        return content;
    }

    public void setContent(CmsContentDTO content) {
        this.content = content;
    }

    public Map<String, String> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, String> attribute) {
        this.attribute = attribute;
    }
}
