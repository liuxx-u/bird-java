package com.bird.web.sso.client.controller;

import com.bird.web.sso.client.WebfluxSsoClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author liuxx
 * @date 2019/4/2
 */
@RestController
@RequestMapping("/sso/client/ticket")
public class WebfluxTicketController {

    private WebfluxSsoClient ssoClient;

    public WebfluxTicketController(WebfluxSsoClient ssoClient){
        this.ssoClient = ssoClient;
    }


    @GetMapping("/removeCache")
    public Mono<String> removeCache(String token) {
        ssoClient.removeTicketCache(token);

        return Mono.just("success");
    }
}
