package com.bird.service.zero.mapper;

import com.bird.core.sso.client.ClientInfo;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.zero.model.Site;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SiteMapper extends AbstractMapper<Site> {

    /**
     * 获取用户可登录的站点信息
     * @param userId
     * @return
     */
    List<ClientInfo> getUserClients(@Param("userId") Long userId);
}
