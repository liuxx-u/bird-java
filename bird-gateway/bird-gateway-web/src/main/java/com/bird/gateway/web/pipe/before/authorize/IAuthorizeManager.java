package com.bird.gateway.web.pipe.before.authorize;

import com.bird.core.session.BirdSession;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author liuxx
 * @date 2018/12/3
 */
public interface IAuthorizeManager {

    /**
     * 解析session信息
     * @param exchange exchange
     * @return session
     */
    BirdSession parseSession(ServerWebExchange exchange);

    /**
     * 校验权限信息
     * @param session session
     * @param permissions permissions
     * @param checkAll checkAll
     * @return true or false
     */
    Boolean checkPermissions(BirdSession session,String[] permissions, boolean checkAll);
}
