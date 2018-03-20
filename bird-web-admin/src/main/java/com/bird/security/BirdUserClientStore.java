package com.bird.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bird.core.sso.client.ClientInfo;
import com.bird.core.sso.client.UserClientStore;
import com.bird.service.zero.SiteService;

import java.util.List;


public class BirdUserClientStore implements UserClientStore {

    @Reference
    private SiteService siteService;

    /**
     * 获取用户能够登录的站点信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<ClientInfo> getUserClients(String userId) {
        return siteService.getUserClients(userId);
    }
}
