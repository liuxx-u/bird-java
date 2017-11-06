package com.bird.service.zero.mapper;

import com.bird.core.mapper.AbstractMapper;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.model.Organization;

import java.util.List;

/**
 * Created by liuxx on 2017/11/1.
 */
public interface OrganizationMapper extends AbstractMapper<Organization> {

    /**
     * 获取组织机构树数据
     * @return
     */
    List<TreeDTO> getOrganizationTreeData();
}
