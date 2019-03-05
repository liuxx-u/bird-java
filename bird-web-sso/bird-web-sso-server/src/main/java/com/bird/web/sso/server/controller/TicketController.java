package com.bird.web.sso.server.controller;

import com.bird.web.sso.server.SsoServer;
import com.bird.web.sso.ticket.TicketInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxx
 * @date 2019/3/4
 */
@RestController
@RequestMapping("/sso/server/ticket")
public class TicketController {

    private SsoServer ssoServer;

    public TicketController(SsoServer ssoServer){
        this.ssoServer = ssoServer;
    }

    /**
     * 根据token获取票据信息
     * @param token token
     * @return 票据信息
     */
    @GetMapping("/get")
    public TicketInfo get(String token){
        return ssoServer.getTicket(token);
    }
}
