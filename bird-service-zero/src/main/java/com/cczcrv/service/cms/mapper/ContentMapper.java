package com.bird.service.cms.mapper;

import com.bird.core.mapper.AbstractMapper;
import com.bird.service.cms.dto.CmsQueryDTO;
import com.bird.service.cms.dto.CmsResultDTO;
import com.bird.service.cms.model.Content;
import com.bird.service.cms.mapper.provider.QueryContentProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by panda on 2017/6/6.
 */

public interface ContentMapper extends AbstractMapper<Content> {

    @SelectProvider(type = QueryContentProvider.class, method = "QueryContentBriefList")
    List<CmsResultDTO> QueryContentBriefList(CmsQueryDTO query);
}
