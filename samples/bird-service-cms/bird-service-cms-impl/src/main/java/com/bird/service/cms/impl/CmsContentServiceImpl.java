package com.bird.service.cms.impl;

import com.bird.core.Check;
import com.bird.service.cms.CmsContentService;
import com.bird.service.cms.dto.CmsFullContentDTO;
import com.bird.service.cms.mapper.CmsContentMapper;
import com.bird.service.cms.model.CmsContent;
import com.bird.service.common.service.AbstractService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@CacheConfig(cacheNames = "cms_content")
@com.alibaba.dubbo.config.annotation.Service
public class CmsContentServiceImpl extends AbstractService<CmsContentMapper,CmsContent> implements CmsContentService {

    /**
     * 保存文章信息（包括自定义属性）
     *
     * @param fullContentDTO
     */
    @Override
    public void saveContent(CmsFullContentDTO fullContentDTO) {
        Check.NotNull(fullContentDTO, "fullContentDTO");

        CmsContent content = dozer.map(fullContentDTO.getContent(), CmsContent.class);
        save(content);

        //编辑模式下，删除现有的属性再重新保存
        if (fullContentDTO.getContent().getId() > 0) {
            mapper.deleteAttribute(content.getId());
        }
        Map<String, String> attribute = fullContentDTO.getAttribute();
        mapper.saveAttribute(content.getId(), attribute);
    }
}
