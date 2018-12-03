package com.bird.gateway.bootstrap.configurer;

import com.bird.core.session.BirdSession;
import com.bird.gateway.web.pipe.before.authorize.IAuthorizeManager;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author liuxx
 * @date 2018/12/3
 */
@Component
public class AuthorizeManager implements IAuthorizeManager {
    @Override
    public BirdSession parseSession(ServerWebExchange exchange) {
        BirdSession session = new BirdSession();
        session.setUserId(1);
        return session;
    }

    @Override
    public Boolean checkPermissions(BirdSession session, String[] permissions, boolean checkAll) {
        return true;
    }
}
