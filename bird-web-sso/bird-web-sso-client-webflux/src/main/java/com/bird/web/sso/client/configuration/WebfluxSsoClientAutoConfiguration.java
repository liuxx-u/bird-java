package com.bird.web.sso.client.configuration;

import com.bird.web.sso.SsoConstant;
import com.bird.web.sso.client.SsoClientProperties;
import com.bird.web.sso.client.WebfluxSsoClient;
import com.bird.web.sso.client.cache.IClientTicketCache;
import com.bird.web.sso.client.controller.WebfluxTicketController;
import com.bird.web.sso.client.remote.IRemoteTicketHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @date 2019/4/2
 */
@Configuration
@AutoConfigureAfter(SsoClientAutoConfiguration.class)
@ConditionalOnProperty(value = SsoConstant.CLIENT_COOKIE_NAME)
public class WebfluxSsoClientAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = SsoConstant.CLIENT_WEBFLUX, havingValue = "true")
    public WebfluxSsoClient ssoClient(SsoClientProperties clientProperties, IRemoteTicketHandler remoteTicketHandler, IClientTicketCache clientTicketCache) {
        return new WebfluxSsoClient(clientProperties, remoteTicketHandler, clientTicketCache);
    }

    @Bean
    @ConditionalOnProperty(value = SsoConstant.CLIENT_WEBFLUX, havingValue = "true")
    public WebfluxTicketController ticketController(WebfluxSsoClient ssoClient) {
        return new WebfluxTicketController(ssoClient);
    }
}
