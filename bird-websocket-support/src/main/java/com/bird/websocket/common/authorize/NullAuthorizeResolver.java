package com.bird.websocket.common.authorize;

/**
 * @author liuxx
 * @since 2020/12/29
 */
public class NullAuthorizeResolver implements IAuthorizeResolver {

    /**
     * 将token解析为系统中的userId
     *
     * @param token token
     * @return userId
     */
    @Override
    public String resolve(String token) {
        return token;
    }
}
