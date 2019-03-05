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

    @GetMapping("/test")
    public TicketInfo getTicket(HttpServletRequest request){
        return ssoClient.getTicket(request);
    }
}
