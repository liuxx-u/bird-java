package com.bird.service.zero;

import com.bird.core.sso.client.ClientInfo;
import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.model.Site;

import java.util.List;

public interface SiteService extends AbstractService<Site> {

    /**
     * 获取用户可登录的站点集合
     * @param userId 用户id
     * @return
     */
    List<ClientInfo> getUserClients(String userId);
}
