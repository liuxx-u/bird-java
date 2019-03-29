package com.bird.web.sso.server.client;

import java.util.List;

/**
 * token与clientHost存储器
 * 存储器有效期应与token的有效期一致，过期后自动删除token与client的关联信息
 *
 * @author liuxx
 * @date 2019/3/29
 */
public interface IClientStore {

    /**
     * 缓存token已登录的客户端信息
     * @param token token
     * @param clientHost clientHost
     */
    void store(String token,String clientHost);

    /**
     * 获取token已登录的所有客户端信息
     * @param token token
     */
    List<String> getAll(String token);

    /**
     * 清除token与client的关联信息
     * @param token
     */
    void remove(String token);
}
