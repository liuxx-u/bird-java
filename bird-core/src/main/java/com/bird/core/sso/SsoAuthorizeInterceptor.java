package com.bird.core.sso;

import com.bird.core.sso.exception.ForbiddenException;
import com.bird.core.sso.exception.UnAuthorizedException;
import com.bird.core.sso.permission.UserPermissionChecker;
import com.bird.core.sso.ticket.TicketHandler;
import com.bird.core.sso.ticket.TicketInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuxx on 2017/5/18.
 */
public class SsoAuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TicketHandler ticketHandler;

    @Autowired
    private SsoAuthorizeManager authorizeManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return false;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        SsoAuthorize authorize = handlerMethod.getMethodAnnotation(SsoAuthorize.class);
        if (authorize != null) {
            TicketInfo ticketInfo = ticketHandler.getTicket(request);
            if (ticketInfo == null) {
                throw new UnAuthorizedException("用户信息已失效.");
            }

            if(!checkAllowHosts(request,ticketInfo)){
                throw new ForbiddenException("用户没有当前站点的登录权限.");
            }

            String[] requirePermissions = authorize.permissions();
            if(requirePermissions.length==0)return true;

            boolean isCheckAll = authorize.isCheckAll();
            UserPermissionChecker permissionChecker = authorizeManager.getUserPermissionChecker();
            if(!permissionChecker.hasPermissions(ticketInfo.getUserId(),requirePermissions,isCheckAll)){
                throw new ForbiddenException("用户没有当前操作的权限.");
            }
        }

        return true;
    }



    private boolean checkAllowHosts(HttpServletRequest request,TicketInfo ticketInfo){
        return true;
    }
}
