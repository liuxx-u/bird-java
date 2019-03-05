package com.bird.web.sso.client;

import com.bird.web.sso.client.remote.IRemoteTicketHandler;
import com.bird.web.sso.ticket.TicketInfo;
import com.bird.web.sso.utils.CookieHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxx
 * @date 2019/3/4
 */
@Slf4j
public class SsoClient {

    private SsoClientProperties clientProperties;
    private IRemoteTicketHandler ticketHandler;
    private Cache<String, TicketInfo> cache;


    public SsoClient(SsoClientProperties clientProperties, IRemoteTicketHandler ticketHandler) {
        this.clientProperties = clientProperties;
        this.ticketHandler = ticketHandler;

        cache = CacheBuilder.newBuilder().expireAfterWrite(clientProperties.getCache(), TimeUnit.MINUTES).build();
    }

    /**
     * 解析票据信息
     *
     * @param request
     * @return
     */
    public TicketInfo getTicket(HttpServletRequest request) {

        String token = this.getToken(request);
        if (StringUtils.isBlank(token)) return null;


        try {
            return cache.get(token, () -> ticketHandler.getTicket(token));
        } catch (Exception e) {
            log.error("sso 客户端票据读取失败", e);
            return null;
        }
    }

    /**
     * 从Request中获取token
     *
     * @param request request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(clientProperties.getCookieName());
        if (StringUtils.isBlank(token)) {
            //header中没有token,则从cookie中获取
            token = CookieHelper.getCookieValue(request, clientProperties.getCookieName());
        }
        return token;
    }
}
