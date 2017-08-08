package com.cczcrv.service.cms.mapper;

import com.cczcrv.core.mapper.AbstractMapper;
import com.cczcrv.service.cms.dto.CmsQueryDTO;
import com.cczcrv.service.cms.dto.CmsResultDTO;
import com.cczcrv.service.cms.model.Content;
import com.cczcrv.service.cms.mapper.provider.QueryContentProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by panda on 2017/6/6.
 */

public interface ContentMapper extends AbstractMapper<Content> {

    @SelectProvider(type = QueryContentProvider.class, method = "QueryContentBriefList")
    List<CmsResultDTO> QueryContentBriefList(CmsQueryDTO query);
}
