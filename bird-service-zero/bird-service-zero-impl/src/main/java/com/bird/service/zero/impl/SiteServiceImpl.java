package com.bird.service.zero.impl;

import com.bird.core.Check;
import com.bird.core.sso.client.ClientInfo;
import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.SiteService;
import com.bird.service.zero.mapper.SiteMapper;
import com.bird.service.zero.model.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "zero_site")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.SiteService")
public class SiteServiceImpl extends AbstractService<SiteMapper,Site> implements SiteService {

    /**
     * 获取用户可登录的站点集合
     * @param userId 用户id
     * @return
     */
    public List<ClientInfo> getUserClients(String userId){
        Check.NotEmpty(userId,"userId");
        Long uid = Long.parseLong(userId);
        return mapper.getUserClients(uid);
    }
}
