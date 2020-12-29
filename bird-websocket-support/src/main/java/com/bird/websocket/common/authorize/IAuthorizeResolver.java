package com.bird.websocket.common.authorize;

/**
 * @author liuxx
 * @since 2020/12/29
 */
public interface IAuthorizeResolver {

    /**
     * 将token解析为系统中的userId
     *
     * @param token token
     * @return userId
     */
    String resolve(String token);
}
