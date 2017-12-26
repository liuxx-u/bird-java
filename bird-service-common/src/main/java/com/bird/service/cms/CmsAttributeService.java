package com.bird.service.cms;

import com.bird.core.service.AbstractService;
import com.bird.service.cms.dto.CmsAttributeDTO;
import com.bird.service.cms.model.CmsAttribute;

import java.util.List;

public interface CmsAttributeService extends AbstractService<CmsAttribute> {

    /**
     * 根据分类id获取属性集合
     * @param classifyId 分类id
     * @return
     */
    List<CmsAttributeDTO> getClassifyAttribute(Long classifyId);
}
