package com.bird.web.sso.client.controller;

import com.bird.web.sso.client.SsoClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxx
 * @date 2019/3/29
 */
@RestController
@RequestMapping("/sso/client/ticket")
public class TicketController {

    private SsoClient ssoClient;

    public TicketController(SsoClient ssoClient) {
        this.ssoClient = ssoClient;
    }

    /**
     * 移除客户端Ticket缓存
     *
     * @param token token
     * @return 票据信息
     */
    @GetMapping("/removeCache")
    public String get(String token) {
        ssoClient.removeTicketCache(token);
        return "success";
    }
}
