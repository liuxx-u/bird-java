package com.bird.service.cms;

import com.bird.service.cms.dto.CmsClassifyDTO;
import com.bird.service.cms.model.CmsClassify;
import com.bird.service.common.service.IService;

public interface CmsClassifyService extends IService<CmsClassify> {

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
