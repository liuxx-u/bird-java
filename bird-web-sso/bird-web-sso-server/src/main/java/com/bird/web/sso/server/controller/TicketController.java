package com.bird.web.sso.server.controller;

import com.bird.web.sso.server.SsoServer;
import com.bird.web.sso.ticket.TicketInfo;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuxx
 * @date 2019/3/4
 */
@RestController
@RequestMapping("/sso/server/ticket")
public class TicketController {

    private SsoServer ssoServer;

    public TicketController(SsoServer ssoServer) {
        this.ssoServer = ssoServer;
    }

    /**
     * 根据token获取票据信息
     *
     * @param clientHost 客户端
     * @param token token
     * @return 票据信息
     */
    @GetMapping("/get")
    public TicketInfo get(String clientHost, String token) {
        return ssoServer.getTicket(clientHost, token);
    }

    /**
     * 刷新票据
     *
     * @param token      token
     * @param ticketInfo 新的票据信息
     */
    @PostMapping("/refresh")
    public void refresh(String token, @RequestBody TicketInfo ticketInfo) {
        ssoServer.refreshTicket(token, ticketInfo);
    }
}
