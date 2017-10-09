package com.bird.service.cms.mapper;

import com.bird.common.dto.AttributeValueDTO;
import com.bird.core.mapper.AbstractMapper;
import com.bird.service.cms.mapper.provider.QueryCmsAttributeProvider;
import com.bird.service.cms.model.ContentAttributeMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by liuxx on 2017/6/27.
 */
public interface ContentAttributeMapMapper extends AbstractMapper<ContentAttributeMap> {

    @SelectProvider(type = QueryCmsAttributeProvider.class, method = "QueryCmsAttributes")
    List<AttributeValueDTO> getCmsAttributeValues(long[] ids, String[] keys);
}
