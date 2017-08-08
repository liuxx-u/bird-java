package com.cczcrv.service.cms.impl;

import com.cczcrv.common.dto.AttributeValueDTO;
import com.cczcrv.core.service.AbstractServiceImpl;
import com.cczcrv.core.utils.CollectionHelper;
import com.cczcrv.core.utils.StringHelper;
import com.cczcrv.service.cms.ContentService;
import com.cczcrv.service.cms.dto.CmsDetailDTO;
import com.cczcrv.service.cms.dto.CmsQueryDTO;
import com.cczcrv.service.cms.dto.CmsResultDTO;
import com.cczcrv.service.cms.mapper.ContentAttributeMapMapper;
import com.cczcrv.service.cms.mapper.ContentMapper;
import com.cczcrv.service.cms.model.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by panda on 2017/6/6.
 */
@Component
@CacheConfig(cacheNames = "content")
@com.alibaba.dubbo.config.annotation.Service
public class ContentServiceImpl extends AbstractServiceImpl<Content> implements ContentService {
    @Autowired
    private ContentAttributeMapMapper contentAttributeMapMapper;
    @Autowired
    private ContentMapper contentMapper;

    /**
     * 通用的新闻查询，支持属性自动组装
     *
     * @param query         筛选条件
     * @param attributeKeys 需要查询并自动组装的属性Key
     * @return
     */
    public List<CmsResultDTO> queryContentList(CmsQueryDTO query, String[] attributeKeys) {
        List<CmsResultDTO> result = contentMapper.QueryContentBriefList(query);
        Collection<Long> idList = CollectionHelper.init(result).select(p -> p.getId());
        long[] ids = idList.stream().mapToLong(t -> t.longValue()).toArray();

        if (ids.length > 0 && attributeKeys != null && attributeKeys.length > 0) {
            Map<Long, Map<String, String>> contentAttributeMap = new HashMap<>();
            List<AttributeValueDTO> attributes = contentAttributeMapMapper.getCmsAttributeValues(ids, attributeKeys);
            for (AttributeValueDTO attribute : attributes) {
                if (!contentAttributeMap.containsKey(attribute.getId())) {
                    contentAttributeMap.put(attribute.getId(), getEmptyAttributeKeyMap(attributeKeys));
                }
                contentAttributeMap.get(attribute.getId()).put(StringHelper.toCamelCase(attribute.getKey()), StringHelper.trim(attribute.getValue(),','));
            }

            for (CmsResultDTO content : result) {
                Map<String, String> attributeMap = contentAttributeMap.containsKey(content.getId())
                        ? contentAttributeMap.get(content.getId())
                        : getEmptyAttributeKeyMap(attributeKeys);

                content.setAttribute(attributeMap);
            }
        }
        return result;
    }

    /**
     * 获取新闻详情，支持属性自动组装
     * @param id 新闻Id
     * @param attributeKeys
     * @return
     */
    public CmsDetailDTO getContent(Long id, String[] attributeKeys) {
        Content content = queryById(id);
        if (content == null) return null;
        CmsDetailDTO result = new CmsDetailDTO(content);

        if (attributeKeys != null && attributeKeys.length > 0) {
            long[] ids = new long[]{id};
            List<AttributeValueDTO> attributes = contentAttributeMapMapper.getCmsAttributeValues(ids, attributeKeys);
            Map<String, String> attributeMap = getEmptyAttributeKeyMap(attributeKeys);
            for (AttributeValueDTO attribute : attributes) {
                attributeMap.put(StringHelper.toCamelCase(attribute.getKey()), StringHelper.trim(attribute.getValue(), ','));
            }
            result.setAttribute(attributeMap);
        }
        return result;
    }

    /**
     * 根据属性键集合生成空的MAP
     * @param attributeKeys
     * @return
     */
    private Map<String,String> getEmptyAttributeKeyMap(String[] attributeKeys){
        Map<String,String> attributeMap=new HashMap<>();
        for (String key : attributeKeys){
            attributeMap.put(key,"");
        }
        return attributeMap;
    }
}
