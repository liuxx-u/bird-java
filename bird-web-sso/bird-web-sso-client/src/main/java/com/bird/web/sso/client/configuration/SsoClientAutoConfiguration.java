package com.bird.web.sso.client.configuration;

import com.bird.web.sso.SsoConstant;
import com.bird.web.sso.client.SsoClient;
import com.bird.web.sso.client.SsoClientProperties;
import com.bird.web.sso.client.controller.TicketController;
import com.bird.web.sso.client.remote.DefaultRemoteTicketHandler;
import com.bird.web.sso.client.remote.IRemoteTicketHandler;
import com.bird.web.sso.event.ISsoEventListener;
import com.google.common.eventbus.EventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/3/5
 */
@Configuration
@ConditionalOnProperty(value = SsoConstant.CLIENT_COOKIE_NAME)
public class SsoClientAutoConfiguration {


    @Bean
    @ConfigurationProperties(prefix = SsoConstant.PREFIX_CLIENT)
    public SsoClientProperties clientProperties(){
        return new SsoClientProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public IRemoteTicketHandler remoteTicketHandler(SsoClientProperties clientProperties){
        return new DefaultRemoteTicketHandler(clientProperties);
    }

    @Bean
    public SsoClient ssoClient(SsoClientProperties clientProperties,IRemoteTicketHandler remoteTicketHandler){
        return new SsoClient(clientProperties,remoteTicketHandler);
    }

    @Bean
    public TicketController ticketController(SsoClient ssoClient){
        return new TicketController(ssoClient);
    }

    @Bean
    @ConditionalOnBean(ISsoEventListener.class)
    @ConditionalOnMissingBean
    public EventBus eventBus(List<ISsoEventListener> listeners){
        EventBus eventBus = new EventBus();
        for (ISsoEventListener listener:listeners) {
            eventBus.register(listener);
        }
        return eventBus;
    }
}
