package com.bird.web.boot.starter.sso;

import com.bird.web.sso.SsoAuthorizeManager;
import com.bird.web.sso.client.IUserClientStore;
import com.bird.web.sso.permission.IUserPermissionChecker;
import com.bird.web.sso.ticket.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * @author liuxx
 * @date 2018/3/24
 */
@Configuration
@ConditionalOnProperty(value = SsoConstant.COOKIE_NAME)
@EnableConfigurationProperties(SsoProperties.class)
public class SsoConfigure {

    @Inject
    private SsoProperties ssoProperties;

    /**
     * 注入默认的IUserClientStore
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IUserClientStore userClientStore(){
        return new NullUserClientStore();
    }

    /**
     * 注入默认的IUserPermissionChecker
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IUserPermissionChecker userPermissionChecker(){
        return new NullUserPermissionChecker();
    }

    /**
     * 注入默认的ITicketSessionStore
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ITicketSessionStore ticketSessionStore(){
        return new RedisTicketSessionStore();
    }

    /**
     * 注入默认的票据加密器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ITicketProtector ticketProtector(){
        return new DesTicketProtector();
    }

    @Bean
    public TicketHandler ticketHandler(){
        TicketHandler ticketHandler = new TicketHandler();
        ticketHandler.setAutoRefresh(ssoProperties.getAutoRefresh());
        return ticketHandler;
    }

    @Bean
    public SsoAuthorizeManager ssoAuthorizeManager(){
        SsoAuthorizeManager authorizeManager = new SsoAuthorizeManager();
        authorizeManager.setCookieName(ssoProperties.getCookieName());
        authorizeManager.setLoginPath(ssoProperties.getLoginPath());

        return authorizeManager;
    }
}
