package com.bird.websocket.common.configuration;

import com.bird.web.sso.client.SsoClient;
import com.bird.websocket.common.authorize.IAuthorizeResolver;
import com.bird.websocket.common.authorize.SsoClientAuthorizeResolver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/12/29
 */
@Configuration
@ConditionalOnClass(SsoClient.class)
@ConditionalOnBean(SsoClient.class)
@AutoConfigureAfter(name = {"com.bird.web.sso.client.configuration.SsoClientAutoConfiguration","com.bird.web.sso.client.configuration.WebfluxSsoClientAutoConfiguration"})
public class SsoClientWebSocketAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IAuthorizeResolver.class)
    public IAuthorizeResolver ssoClientAuthorizeResolver(SsoClient ssoClient) {
        return new SsoClientAuthorizeResolver(ssoClient);
    }
}
