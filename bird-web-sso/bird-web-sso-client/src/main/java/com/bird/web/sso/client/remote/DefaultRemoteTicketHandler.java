package com.bird.web.sso.client.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bird.web.sso.client.SsoClientProperties;
import com.bird.web.sso.client.event.SsoClientFetchTicketEvent;
import com.bird.web.sso.ticket.TicketInfo;
import com.bird.web.sso.utils.HttpClient;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/3/4
 */
@Slf4j
public class DefaultRemoteTicketHandler implements IRemoteTicketHandler {

    private SsoClientProperties clientProperties;
    private final static String GET_TICKET_URL = "/sso/server/ticket/get";
    private final static String REFRESH_TICKET_URL = "/sso/server/ticket/refresh";

    @Autowired(required = false)
    private EventBus eventBus;

    public DefaultRemoteTicketHandler(SsoClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    /**
     * 从sso服务器获取票据信息
     *
     * @param token token
     * @return 票据信息
     */
    @Override
    public TicketInfo getTicket(String token) {
        if (StringUtils.isBlank(token)) return null;

        Integer retryCount = 3;
        String url = clientProperties.getServer() + GET_TICKET_URL + "?token=" + token + "&clientHost=" + clientProperties.getHost();
        List<String> headers = Arrays.asList("Accept-Encoding", "gzip,deflate,sdch");

        int resCode = 0;
        TicketInfo ticketInfo;
        do {
            SsoClientFetchTicketEvent fetchTicketEvent = new SsoClientFetchTicketEvent(token);
            try {
                HttpClient.HttpResult result = HttpClient.httpGet(url, headers, null, HttpClient.DEFAULT_CONTENT_TYPE);
                if (HttpURLConnection.HTTP_OK != result.code) {
                    throw new IOException("Error while requesting: " + url + "'. Server returned: " + result.code);
                }
                resCode = result.code;
                ticketInfo = JSONObject.parseObject(result.content, TicketInfo.class);
                fetchTicketEvent.success(ticketInfo);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                ticketInfo = null;
                fetchTicketEvent.fail(ex.getMessage());
            } finally {
                if (eventBus != null) {
                    eventBus.post(fetchTicketEvent);
                }
            }
        } while (HttpURLConnection.HTTP_OK != resCode && retryCount-- > 0);

        return ticketInfo;
    }

    /**
     * 刷新服务端票据信息
     *
     * @param token      token
     * @param ticketInfo 新的票据信息
     */
    @Override
    public Boolean refreshTicket(String token, TicketInfo ticketInfo) {
        if (StringUtils.isBlank(token) || ticketInfo == null) return false;

        Integer retryCount = 3;
        String url = clientProperties.getServer() + REFRESH_TICKET_URL + "?token=" + token;

        int resCode = 0;
        do {
            try {
                HttpClient.HttpResult result = HttpClient.httpPost(url, null, JSON.toJSONString(ticketInfo));
                if (HttpURLConnection.HTTP_OK != result.code) {
                    throw new IOException("Error while requesting: " + url + "'. Server returned: " + result.code);
                }
                resCode = result.code;
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } while (HttpURLConnection.HTTP_OK != resCode && retryCount-- > 0);

        return HttpURLConnection.HTTP_OK == resCode;
    }
}
