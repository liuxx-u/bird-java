package com.bird.web.sso.client.sample.controller;

import com.bird.web.sso.client.SsoClient;
import com.bird.web.sso.ticket.TicketInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2019/3/5
 */
@RestController
public class TestController {

    @Autowired
    private SsoClient ssoClient;

    @GetMapping("/get")
    public TicketInfo getTicket(HttpServletRequest request){
        return ssoClient.getTicket(request);
    }

    @GetMapping("/refresh")
    public String refreshTicket(HttpServletRequest request){

        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setUserId("17");
        ticketInfo.setName("new Ticket");
        ticketInfo.setClaim("mmkkk","yyyyy");

        ssoClient.refreshTicket(request,ticketInfo);
        return "xxx";
    }

    @GetMapping("/remove")
    public String removeTicket(HttpServletRequest request){

        ssoClient.removeTicketCache(request);
        return "yyy";
    }
}
