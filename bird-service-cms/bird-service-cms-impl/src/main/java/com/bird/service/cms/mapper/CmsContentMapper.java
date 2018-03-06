package com.bird.service.cms.mapper;

import com.bird.service.cms.model.CmsContent;
import com.bird.service.common.mapper.AbstractMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CmsContentMapper extends AbstractMapper<CmsContent> {

    /**
     * 保存文章自定义属性信息
     *
     * @param contentId
     * @return
     */
    void saveAttribute(@Param("contentId") Long contentId, @Param("attribute") Map<String, String> attribute);

    /**
     * 删除文章自定义属性信息
     * @param contentId
     */
    void deleteAttribute(@Param("contentId") Long contentId);
}
