package com.bird.service.cms;

import com.bird.core.service.AbstractService;
import com.bird.service.cms.dto.CmsClassifyDTO;
import com.bird.service.cms.model.CmsClassify;

public interface CmsClassifyService extends AbstractService<CmsClassify> {

    /**
     * 获取分类
     * @return
     */
    CmsClassifyDTO getClassify(Long id);

    /**
     * 保存分类
     * @param dto
     */
    void saveClassify(CmsClassifyDTO dto);
}
