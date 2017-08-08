package com.cczcrv.service.cms.mapper;

import com.cczcrv.common.dto.AttributeValueDTO;
import com.cczcrv.core.mapper.AbstractMapper;
import com.cczcrv.service.cms.mapper.provider.QueryCmsAttributeProvider;
import com.cczcrv.service.cms.model.ContentAttributeMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by liuxx on 2017/6/27.
 */
public interface ContentAttributeMapMapper extends AbstractMapper<ContentAttributeMap> {

    @SelectProvider(type = QueryCmsAttributeProvider.class, method = "QueryCmsAttributes")
    List<AttributeValueDTO> getCmsAttributeValues(long[] ids, String[] keys);
}
