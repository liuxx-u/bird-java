package com.bird.web.sso;

import com.bird.core.Check;
import com.bird.web.sso.client.ClientInfo;
import com.bird.web.sso.client.IUserClientStore;
import com.bird.web.sso.permission.IUserPermissionChecker;
import com.bird.web.sso.ticket.TicketInfo;
import com.bird.web.sso.ticket.ITicketProtector;
import com.bird.web.sso.ticket.ITicketSessionStore;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxx on 2017/5/18.
 */
public class SsoAuthorizeManager {
    static final String User_Client_CliamKey="user.clients";
    private String cookieName;
    private Integer expire; //单位：分
    private String loginPath;

    /**
     * 票据加密器
     */
    @Inject
    private ITicketProtector protector;

    /**
     * 前端js执行代码生成器
     */
    private JavascriptCodeGenerator Javascript = new JavascriptCodeGenerator();

    /**
     * 用户-站点信息存储器
     */
    @Inject
    private IUserClientStore userClientStore;

    /**
     * 票据信息存储器
     */
    @Inject
    private ITicketSessionStore sessionStore;

    /**
     * 用户权限检查器
     */
    @Inject
    private IUserPermissionChecker userPermissionChecker;

    /**
     * 生成登录成功的前端js执行代码
     * @param ticketInfo
     * @param redirectUrl
     * @return
     */
    public String getLoginJsCode(HttpServletResponse response, TicketInfo ticketInfo, String redirectUrl){
        List<ClientInfo> clients = userClientStore.getUserClients(ticketInfo.getUserId());

        //确保ticket信息中包含允许登录的站点信息
        ticketInfo.setClaim(User_Client_CliamKey,clients);
        String token = generateToken(ticketInfo);

        //用户中心写入Cookie
        Cookie cookie = new Cookie(this.cookieName, token.trim());
        cookie.setMaxAge(this.expire * 60);// 设置为有效期
        cookie.setPath("/");
        response.addCookie(cookie);

        //生成通知其他站点的js代码
        List<String> loginNotifyUrls = new ArrayList<>();
        for (ClientInfo client:clients) {
            if(StringUtils.isBlank(client.getLoginNotifyUrl()))continue;
            loginNotifyUrls.add(client.getLoginNotifyUrl());
        }
        return Javascript.getLoginCode(token,loginNotifyUrls,redirectUrl);
    }

    /**
     * 生成登录失败的前端js执行代码
     * @param errorInfo
     * @return
     */
    public String getErrorJsCode(String errorInfo){
        return Javascript.getErrorCode(errorInfo);
    }

    /**
     * 注销，只删除sessionStore中的数据
     * @return
     */
    public void logout(String token) {

        if (sessionStore != null) {
            sessionStore.removeTicket(token);
        }
    }

    /**
     * 根据ticket生成token
     * @param ticketInfo 票据信息
     * @return
     */
    public String generateToken(TicketInfo ticketInfo) {
        if (ticketInfo.getExpireTime() == null) {
            Date creationTime = ticketInfo.getCreationTime();
            long expire = creationTime.getTime() + this.expire * 60 * 1000L;
            ticketInfo.setExpireTime(new Date(expire));
        }

        if (sessionStore != null) {
            return sessionStore.storeTicket(ticketInfo);
        }
        return protector.protect(ticketInfo);
    }

    /**
     * 刷新票据信息
     * @param request
     * @param ticketInfo
     */
    public void refreshToken(HttpServletRequest request, TicketInfo ticketInfo) {
        Check.NotNull(ticketInfo, "ticketInfo");

        //先从header中获取token
        String token = request.getHeader(this.cookieName);
        if (StringUtils.isBlank(token)) {
            //header中没有token,则从cookie中获取
            Cookie[] cookies = request.getCookies();
            if (cookies == null) return;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(this.cookieName)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        sessionStore.refreshTicket(token, ticketInfo, this.expire * 60 * 1000);
    }


    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public ITicketProtector getProtector() {
        return protector;
    }

    public void setProtector(ITicketProtector protector) {
        this.protector = protector;
    }

    public IUserClientStore getUserClientStore() {
        return userClientStore;
    }

    public void setUserClientStore(IUserClientStore userClientStore) {
        this.userClientStore = userClientStore;
    }

    public ITicketSessionStore getSessionStore() {
        return sessionStore;
    }

    public void setSessionStore(ITicketSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public IUserPermissionChecker getUserPermissionChecker() {
        return userPermissionChecker;
    }

    public void setUserPermissionChecker(IUserPermissionChecker userPermissionChecker) {
        this.userPermissionChecker = userPermissionChecker;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}
