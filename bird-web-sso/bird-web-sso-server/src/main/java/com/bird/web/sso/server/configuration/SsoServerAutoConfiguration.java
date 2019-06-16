package com.bird.web.sso.server.configuration;

import com.bird.web.sso.SsoConstant;
import com.bird.web.sso.event.ISsoEventListener;
import com.bird.web.sso.server.SsoServer;
import com.bird.web.sso.server.SsoServerProperties;
import com.bird.web.sso.server.client.CacheClientStore;
import com.bird.web.sso.server.client.IClientStore;
import com.bird.web.sso.server.controller.TicketController;
import com.bird.web.sso.server.ticket.CacheTicketSessionStore;
import com.bird.web.sso.server.ticket.ITicketProtector;
import com.bird.web.sso.server.ticket.ITicketSessionStore;
import com.bird.web.sso.server.ticket.JwtTicketProtector;
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
@ConditionalOnProperty(value = SsoConstant.SERVER_COOKIE_NAME)
@EnableConfigurationProperties(SsoServerProperties.class)
public class SsoServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = SsoConstant.SERVER_USE_SESSION_STORE, havingValue = "true", matchIfMissing = true)
    public ITicketSessionStore ticketSessionStore(SsoServerProperties serverProperties) {
        return new CacheTicketSessionStore(serverProperties.getExpire());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = SsoConstant.SERVER_USE_SESSION_STORE, havingValue = "false")
    public ITicketProtector ticketProtector() {
        return new JwtTicketProtector();
    }

    @Bean
    @ConditionalOnMissingBean
    public IClientStore clientStore(SsoServerProperties serverProperties) {
        return new CacheClientStore(serverProperties.getExpire());
    }

    @Bean
    @ConditionalOnProperty(value = SsoConstant.SERVER_USE_SESSION_STORE, havingValue = "true", matchIfMissing = true)
    public SsoServer ssoServer(SsoServerProperties serverProperties,IClientStore clientStore,ITicketSessionStore ticketSessionStore) {
        return new SsoServer(serverProperties, clientStore, ticketSessionStore);
    }

    @Bean
    @ConditionalOnProperty(value = SsoConstant.SERVER_USE_SESSION_STORE, havingValue = "false")
    public SsoServer ssoServer(SsoServerProperties serverProperties,IClientStore clientStore,ITicketProtector ticketProtector) {
        return new SsoServer(serverProperties, clientStore, ticketProtector);
    }

    @Bean
    public TicketController ticketController(SsoServer ssoServer) {
        return new TicketController(ssoServer);
    }

    @Bean
    @ConditionalOnBean(ISsoEventListener.class)
    public EventBus eventBus(List<ISsoEventListener> listeners) {
        EventBus eventBus = new EventBus();
        for (ISsoEventListener listener : listeners) {
            eventBus.register(listener);
        }
        return eventBus;
    }
}
