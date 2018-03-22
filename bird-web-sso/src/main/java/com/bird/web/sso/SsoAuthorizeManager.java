package com.bird.web.sso;

import com.bird.web.sso.client.ClientInfo;
import com.bird.web.sso.client.UserClientStore;
import com.bird.web.sso.permission.UserPermissionChecker;
import com.bird.web.sso.ticket.TicketInfo;
import com.bird.web.sso.ticket.TicketProtector;
import com.bird.web.sso.ticket.TicketSessionStore;
import org.apache.commons.lang3.StringUtils;

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
    private TicketProtector protector;

    /**
     * 前端js执行代码生成器
     */
    private JavascriptCodeGenerator Javascript = new JavascriptCodeGenerator();

    /**
     * 用户-站点信息存储器
     */
    private UserClientStore userClientStore;

    /**
     * 票据信息存储器
     */
    private TicketSessionStore sessionStore;

    /**
     * 用户权限检查器
     */
    private UserPermissionChecker userPermissionChecker;

    /**
     * 生成登录成功的前端js执行代码
     * @param ticketInfo
     * @param redirectUrl
     * @return
     */
    public String getLoginJsCode(TicketInfo ticketInfo,String redirectUrl){
        List<ClientInfo> clients = userClientStore.getUserClients(ticketInfo.getUserId());

        //确保ticket信息中包含允许登录的站点信息
        ticketInfo.setClaim(User_Client_CliamKey,clients);
        String token = generateToken(ticketInfo);

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
    private String generateToken(TicketInfo ticketInfo) {
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

    public TicketProtector getProtector() {
        return protector;
    }

    public void setProtector(TicketProtector protector) {
        this.protector = protector;
    }

    public UserClientStore getUserClientStore() {
        return userClientStore;
    }

    public void setUserClientStore(UserClientStore userClientStore) {
        this.userClientStore = userClientStore;
    }

    public TicketSessionStore getSessionStore() {
        return sessionStore;
    }

    public void setSessionStore(TicketSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public UserPermissionChecker getUserPermissionChecker() {
        return userPermissionChecker;
    }

    public void setUserPermissionChecker(UserPermissionChecker userPermissionChecker) {
        this.userPermissionChecker = userPermissionChecker;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}
