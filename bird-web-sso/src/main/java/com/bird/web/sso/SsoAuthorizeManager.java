package com.bird.web.sso;

import com.bird.core.Check;
import com.bird.web.common.utils.CookieHelper;
import com.bird.web.sso.client.ClientInfo;
import com.bird.web.sso.client.IUserClientStore;
import com.bird.web.sso.ticket.ITicketProtector;
import com.bird.web.sso.ticket.ITicketSessionStore;
import com.bird.web.sso.ticket.TicketInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author liuxx
 * @date 2017/5/18
 */
public class SsoAuthorizeManager {
    public static final String TICKET_ATTRIBUTE_KEY = "sso-ticket";

    private String cookieName;
    /**
     * 有效时间,单位：分
     */
    private Integer expire = 60;
    private Boolean useSessionStore = true;

    /**
     * 票据加密器
     */
    @Autowired(required = false)
    private ITicketProtector protector;

    /**
     * 票据信息存储器
     */
    @Autowired(required = false)
    private ITicketSessionStore sessionStore;

    /**
     * 站点信息储存器
     */
    @Autowired
    private IUserClientStore userClientStore;

    /**
     * 获取请求中的票据信息
     * @param request
     * @return
     */
    public TicketInfo getTicket(HttpServletRequest request){
        Object ticket = request.getAttribute(TICKET_ATTRIBUTE_KEY);
        if(ticket == null)return null;
        return (TicketInfo)ticket;
    }

    /**
     * 登录，将token写入cookie
     * @param ticketInfo 票据信息
     * @return token
     */
    public String login(HttpServletResponse response,TicketInfo ticketInfo) {
        if (ticketInfo.getExpireTime() == null) {
            Date creationTime = ticketInfo.getCreationTime();
            long expire = creationTime.getTime() + this.expire * 60 * 1000L;
            ticketInfo.setExpireTime(new Date(expire));
        }

        //确保ticket信息中包含允许登录的站点信息
        List<ClientInfo> clients = userClientStore.getUserClients(ticketInfo.getUserId());
        List<String> allowHosts = clients == null
                ? new ArrayList<>()
                : clients.stream().map(p -> p.getHost()).collect(Collectors.toList());

        ticketInfo.setClaim(IUserClientStore.CLAIM_KEY, allowHosts);

        String token = useSessionStore
                ? sessionStore.storeTicket(ticketInfo)
                : protector.protect(ticketInfo);

        //用户中心写入Cookie
        CookieHelper.setCookie(response, this.cookieName, StringUtils.strip(token), this.expire * 60);
        return token;
    }

    /**
     * 注销
     * 1、清除SessionStore；2、清除Cookie
     * @return
     */
    public void logout(HttpServletRequest request,HttpServletResponse response) {
        String token = getToken(request);

        if (!StringUtils.isEmpty(token)) {
            //清除SessionStore
            if (sessionStore != null) {
                sessionStore.removeTicket(token);
            }

            //清除Cookie
            CookieHelper.removeCookie(request,response,this.cookieName);
        }
    }

    /**
     * 刷新票据信息，仅在sessionStore模式下可用
     * @param request
     * @param ticketInfo
     */
    public void refreshToken(HttpServletRequest request, TicketInfo ticketInfo) {
        Check.NotNull(ticketInfo, "ticketInfo");

        String token = getToken(request);

        if (StringUtils.isNotBlank(token) && sessionStore != null) {
            sessionStore.refreshTicket(token, ticketInfo, this.expire * 60 * 1000);
        }
    }

    /**
     * 从HttpServletRequest中获取token
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request){
        //先从header中获取token
        String token = request.getHeader(this.cookieName);
        if (StringUtils.isBlank(token)) {
            token = CookieHelper.getCookieValue(request,this.cookieName);
        }
        return token;
    }


    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public ITicketProtector getProtector() {
        return protector;
    }

    public void setProtector(ITicketProtector protector) {
        this.protector = protector;
    }

    public ITicketSessionStore getSessionStore() {
        return sessionStore;
    }

    public void setSessionStore(ITicketSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public Boolean getUseSessionStore() {
        return useSessionStore;
    }

    public void setUseSessionStore(Boolean useSessionStore) {
        this.useSessionStore = useSessionStore;
    }
}
