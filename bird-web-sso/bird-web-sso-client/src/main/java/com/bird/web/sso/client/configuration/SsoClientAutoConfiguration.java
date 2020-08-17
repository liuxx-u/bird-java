package com.bird.web.sso.client.configuration;

import com.bird.web.sso.SsoConstant;
import com.bird.web.sso.client.SsoClient;
import com.bird.web.sso.client.SsoClientProperties;
import com.bird.web.sso.client.cache.DefaultClientTicketCache;
import com.bird.web.sso.client.cache.IClientTicketCache;
import com.bird.web.sso.client.controller.ClientTicketController;
import com.bird.web.sso.client.remote.DefaultRemoteTicketHandler;
import com.bird.web.sso.client.remote.IRemoteTicketHandler;
import com.bird.web.sso.event.ISsoEventListener;
import com.google.common.eventbus.EventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/3/5
 */
@Configuration
@ConditionalOnProperty(value = SsoConstant.CLIENT_COOKIE_NAME)
@EnableConfigurationProperties(SsoClientProperties.class)
public class SsoClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IRemoteTicketHandler remoteTicketHandler(SsoClientProperties clientProperties) {
        return new DefaultRemoteTicketHandler(clientProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public IClientTicketCache clientTicketCache(SsoClientProperties clientProperties, IRemoteTicketHandler ticketHandler) {
        return new DefaultClientTicketCache(clientProperties, ticketHandler);
    }

    @Bean
    @ConditionalOnProperty(value = SsoConstant.CLIENT_WEBFLUX, havingValue = "false", matchIfMissing = true)
    public SsoClient ssoClient(SsoClientProperties clientProperties, IRemoteTicketHandler remoteTicketHandler, IClientTicketCache clientTicketCache) {
        return new SsoClient(clientProperties, remoteTicketHandler, clientTicketCache);
    }

    @Bean
    @ConditionalOnProperty(value = SsoConstant.CLIENT_WEBFLUX, havingValue = "false", matchIfMissing = true)
    public ClientTicketController clientTicketController(SsoClient ssoClient) {
        return new ClientTicketController(ssoClient);
    }

    @Bean
    @ConditionalOnBean(ISsoEventListener.class)
    @ConditionalOnMissingBean
    public EventBus eventBus(List<ISsoEventListener> listeners) {
        EventBus eventBus = new EventBus();
        for (ISsoEventListener listener : listeners) {
            eventBus.register(listener);
        }
        return eventBus;
    }
}
