package com.bird.websocket.common.configuration;

import com.bird.websocket.common.ITokenSessionStorage;
import com.bird.websocket.common.ITokenUserStorage;
import com.bird.websocket.common.IUserTokensStorage;
import com.bird.websocket.common.authorize.IAuthorizeResolver;
import com.bird.websocket.common.authorize.NullAuthorizeResolver;
import com.bird.websocket.common.server.BasicSessionDirectory;
import com.bird.websocket.common.server.ISessionDirectory;
import com.bird.websocket.common.server.WebSocketPublisher;
import com.bird.websocket.common.server.WebSocketServer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author liuxx
 * @since 2020/12/29
 */
@Configuration
@AutoConfigureAfter({SsoClientWebSocketAutoConfiguration.class, WebSocketStorageAutoConfiguration.class})
public class WebSocketAutoConfiguration {

    /**
     * ServerEndpointExporter 作用
     * <p>
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 注册 默认的token解析器
     */
    @Bean
    @ConditionalOnMissingBean(IAuthorizeResolver.class)
    public IAuthorizeResolver nullAuthorizeResolver() {
        return new NullAuthorizeResolver();
    }

    /**
     * 注册 Token-User 字典
     */
    @Bean
    public ISessionDirectory defaultSessionDirectory(IAuthorizeResolver authorizeResolver,
                                                     IUserTokensStorage userTokensStorage,
                                                     ITokenUserStorage tokenUserStorage,
                                                     ITokenSessionStorage tokenSessionStorage) {
        return new BasicSessionDirectory(userTokensStorage, tokenUserStorage, tokenSessionStorage, authorizeResolver);
    }

    /**
     * 注册 默认的websocket服务端
     */
    @Bean
    public WebSocketServer webSocketServer() {
        return new WebSocketServer();
    }

    /**
     * 注册 websocket消息发送者
     */
    @Bean
    public WebSocketPublisher webSocketPublisher(ISessionDirectory sessionDirectory) {
        return new WebSocketPublisher(sessionDirectory);
    }
}
