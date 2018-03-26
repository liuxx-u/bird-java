package com.bird.web.boot.starter.sso;

import com.bird.web.sso.client.ClientInfo;
import com.bird.web.sso.client.IUserClientStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @date 2018/3/26
 */
public class NullUserClientStore implements IUserClientStore {
    /**
     * 获取用户能够登录的站点信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<ClientInfo> getUserClients(String userId) {
        return new ArrayList<>();
    }
}
