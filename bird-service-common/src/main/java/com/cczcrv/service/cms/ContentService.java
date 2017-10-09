package com.bird.service.cms;

import com.bird.core.service.AbstractService;
import com.bird.service.cms.dto.CmsDetailDTO;
import com.bird.service.cms.dto.CmsQueryDTO;
import com.bird.service.cms.dto.CmsResultDTO;
import com.bird.service.cms.model.Content;

import java.util.List;

/**
 * Created by liuxx on 2017/7/31.
 */
public interface ContentService extends AbstractService<Content> {

    /**
     * 通用的新闻查询，支持属性自动组装
     *
     * @param query         筛选条件
     * @param attributeKeys 需要查询并自动组装的属性Key
     * @return
     */
    List<CmsResultDTO> queryContentList(CmsQueryDTO query, String[] attributeKeys);

    /**
     * 获取新闻详情，支持属性自动组装
     * @param id 新闻Id
     * @param attributeKeys
     * @return
     */
    CmsDetailDTO getContent(Long id, String[] attributeKeys);
}
