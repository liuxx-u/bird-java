package com.bird.web.sso.client;

import java.util.List;

public interface UserClientStore {
    /**
     * 获取用户能够登录的站点信息
     * @param userId
     * @return
     */
    List<ClientInfo> getUserClients(String userId);
}
