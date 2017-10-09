package com.bird.service.cms.dto;

import com.bird.core.service.page.PagedQueryParam;

/**
 * Created by liuxx on 2017/6/27.
 */
public class CmsQueryDTO extends PagedQueryParam {
    private String queryKey;
    private int classifyId;

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }
}
