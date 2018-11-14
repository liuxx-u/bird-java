package com.bird.web.boot.starter.sso;

import com.bird.web.common.session.IServletSessionResolvor;
import com.bird.web.sso.SsoAuthorizeManager;
import com.bird.web.sso.SsoSessionResolvor;
import com.bird.web.sso.client.IUserClientStore;
import com.bird.web.sso.permission.IUserPermissionChecker;
import com.bird.web.sso.ticket.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @date 2018/3/24
 */
@Configuration
@ConditionalOnProperty(value = SsoConstant.COOKIE_NAME)
@EnableConfigurationProperties(SsoProperties.class)
public class SsoConfigurer {

    @Autowired
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
    @ConditionalOnProperty(value = SsoConstant.USE_SESSTION_STRORE,havingValue = "true",matchIfMissing = true)
    @Bean
    @ConditionalOnMissingBean
    public ITicketSessionStore ticketSessionStore() {
        return new RedisTicketSessionStore();
    }

    /**
     * 注入默认的票据加密器
     * @return
     */
    @ConditionalOnProperty(value = SsoConstant.USE_SESSTION_STRORE,havingValue = "false")
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
        authorizeManager.setExpire(ssoProperties.getExpire());
        authorizeManager.setUseSessionStore(ssoProperties.getUseSessionStore());

        return authorizeManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public IServletSessionResolvor servletSessionResolvor(){
        return new SsoSessionResolvor();
    }
}
