package com.bird.websocket.common.authorize;

import com.bird.web.sso.client.SsoClient;
import com.bird.web.sso.ticket.ClientTicket;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @since 2020/12/29
 */
public class SsoClientAuthorizeResolver implements IAuthorizeResolver {

    private final SsoClient ssoClient;

    public SsoClientAuthorizeResolver(SsoClient ssoClient){
        this.ssoClient = ssoClient;
    }

    /**
     * 将token解析为系统中的userId
     *
     * @param token token
     * @return userId
     */
    @Override
    public String resolve(String token) {
        ClientTicket clientTicket = ssoClient.getTicket(token);

        return clientTicket == null ? StringUtils.EMPTY : clientTicket.getUserId();
    }
}
