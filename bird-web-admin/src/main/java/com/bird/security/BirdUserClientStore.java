package com.bird.security;

import com.bird.core.sso.client.ClientInfo;
import com.bird.core.sso.client.UserClientStore;
import com.bird.service.zero.SiteService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class BirdUserClientStore implements UserClientStore {

    @Autowired
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
