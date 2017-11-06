package com.bird.service.zero.impl;

import com.bird.core.Check;
import com.bird.core.service.AbstractServiceImpl;
import com.bird.core.service.TreeDTO;
import com.bird.core.utils.DozerHelper;
import com.bird.service.zero.OrganizationService;
import com.bird.service.zero.dto.OrganizationDTO;
import com.bird.service.zero.mapper.OrganizationMapper;
import com.bird.service.zero.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxx on 2017/11/1.
 */
@Service
@CacheConfig(cacheNames = "zero_organization")
@com.alibaba.dubbo.config.annotation.Service
public class OrganizationServiceImpl extends AbstractServiceImpl<Organization> implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private DozerHelper dozerHelper;

    /**
     * 获取组织机构树数据
     *
     * @return
     */
    @Override
    public List<TreeDTO> getOrganizationTreeData() {
        return organizationMapper.getOrganizationTreeData();
    }

    /**
     * 获取组织机构信息
     *
     * @return
     */
    @Override
    public OrganizationDTO getOrganization(Long id) {
        Check.GreaterThan(id,0L,"id");
        Organization organization = queryById(id);
        return dozerHelper.map(organization,OrganizationDTO.class);
    }
}
