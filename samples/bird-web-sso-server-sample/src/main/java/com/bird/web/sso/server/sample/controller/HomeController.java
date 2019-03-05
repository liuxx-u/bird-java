package com.bird.web.sso.server.sample.controller;

import com.bird.web.sso.server.SsoServer;
import com.bird.web.sso.ticket.TicketInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author liuxx
 * @date 2019/3/5
 */
@RestController
public class HomeController {

    @Autowired
    private SsoServer ssoServer;

    @GetMapping("/test")
    public void test(HttpServletResponse response){
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setUserId("17");
        ticketInfo.setName("test");
        ticketInfo.setClaim("xx","yyy");

        ssoServer.login(response,ticketInfo);
    }
}
