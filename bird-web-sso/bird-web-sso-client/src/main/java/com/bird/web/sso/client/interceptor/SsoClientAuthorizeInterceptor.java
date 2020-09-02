package com.bird.web.sso.client.interceptor;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import com.bird.web.sso.client.SsoClient;
import com.bird.web.sso.client.permission.IUserPermissionChecker;
import com.bird.web.sso.ticket.ClientTicket;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * sso-client 用户验证拦截器
 * @author liuxx
 * @date 2019/5/15
 */
public class SsoClientAuthorizeInterceptor extends AbstractAuthorizeInterceptor {

    private final SsoClient ssoClient;
    private final IUserPermissionChecker userPermissionChecker;

    public SsoClientAuthorizeInterceptor(SsoClient ssoClient, IUserPermissionChecker userPermissionChecker) {
        this.ssoClient = ssoClient;
        this.userPermissionChecker = userPermissionChecker;
    }

    /**
     * 从请求中解析Session信息
     *
     * @param request 请求
     * @return session
     */
    @Override
    protected BirdSession getSession(HttpServletRequest request) {
        ClientTicket clientTicket = ssoClient.getTicket(request);

        if (clientTicket == null) {
            return null;
        }

        BirdSession session = new BirdSession();
        session.setUserId(clientTicket.getUserId());
        session.setTenantId(clientTicket.getTenantId());
        session.setUserName(clientTicket.getUserName());
        session.setRealName(clientTicket.getRealName());
        session.setClaims(clientTicket.getClaims());
        session.setCreateTime(new Date());

        SessionContext.setSession(session);
        return session;
    }

    /**
     * 校验权限
     *
     * @param userId      userId
     * @param permissions 权限集合
     * @param checkAll    是否检查全部
     * @return 是否检查通过
     */
    @Override
    protected boolean checkPermissions(String userId, List<String> permissions, boolean checkAll) {
        return this.userPermissionChecker.hasPermissions(userId, permissions, checkAll);
    }
}
