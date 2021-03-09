package com.bird.web.sso.server.controller;

import com.bird.web.sso.server.SsoServer;
import com.bird.web.sso.ticket.ClientTicket;
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
     * @param token      token
     * @return 票据信息
     */
    @GetMapping("/get")
    public ClientTicket get(@RequestParam(defaultValue = "") String appId, @RequestParam(defaultValue = "") String clientHost, String token, @RequestParam(value = "autoRefresh", defaultValue = "true") Boolean autoRefresh) {
        return ssoServer.getClientTicket(appId, clientHost, token, autoRefresh);
    }

    /**
     * 刷新票据
     *
     * @param token        token
     * @param clientTicket 新的客户端票据信息
     */
    @PostMapping("/refresh")
    public void refresh(@RequestParam(defaultValue = "") String appId, String token, @RequestBody ClientTicket clientTicket) {
        ssoServer.refreshClientTicket(token, appId, clientTicket);
    }
}
