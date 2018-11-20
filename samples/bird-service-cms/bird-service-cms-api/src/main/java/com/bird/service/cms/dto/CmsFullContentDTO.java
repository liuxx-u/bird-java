package com.bird.service.cms.dto;

import com.bird.service.common.service.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CmsFullContentDTO extends AbstractDTO {
    private CmsContentDTO content;
    private Map<String,String> attribute;

    public CmsFullContentDTO(){
        attribute = new HashMap<>();
    }
}
