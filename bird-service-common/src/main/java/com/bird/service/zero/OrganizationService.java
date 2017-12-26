package com.bird.service.zero;

import com.bird.core.service.AbstractService;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.dto.OrganizationDTO;
import com.bird.service.zero.model.Organization;

import java.util.List;

/**
 * Created by liuxx on 2017/11/1.
 */
public interface OrganizationService extends AbstractService<Organization> {

    /**
     * 获取组织机构信息
     * @return
     */
    OrganizationDTO getOrganization(Long id);

    /**
     * 保存组织机构
     * @param dto
     */
    void saveOrganization(OrganizationDTO dto);
}
