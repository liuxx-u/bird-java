package com.bird.web.sso.client.remote;

import com.alibaba.fastjson.JSONObject;
import com.bird.web.sso.ticket.TicketInfo;
import lombok.extern.slf4j.Slf4j;

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

    private String server;
    private final static String GET_TICKET_URL = "/sso/server/ticket/get?token=";

    public DefaultRemoteTicketHandler(String server) {
        this.server = server;
    }

    /**
     * 从sso服务器获取票据信息
     *
     * @param token token
     * @return 票据信息
     */
    @Override
    public TicketInfo getTicket(String token) {
        Integer retryCount = 3;
        String url = server + GET_TICKET_URL + token;
        List<String> headers = Arrays.asList("Accept-Encoding", "gzip,deflate,sdch", "Connection", "Keep-Alive");

        int resCode = 0;
        TicketInfo ticketInfo;
        do {
            try {
                HttpClient.HttpResult result = HttpClient.httpGet(url, headers, null, HttpClient.DEFAULT_CONTENT_TYPE);
                if (HttpURLConnection.HTTP_OK != result.code) {
                    throw new IOException("Error while requesting: " + url + "'. Server returned: " + result.code);
                }
                resCode = result.code;
                ticketInfo = JSONObject.parseObject(result.content, TicketInfo.class);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                ticketInfo = null;
            }
        } while (HttpURLConnection.HTTP_OK != resCode && retryCount-- > 0);

        return ticketInfo;
    }

    @Override
    public void removeTicket(String token, Boolean notifyAll) {

    }
}
