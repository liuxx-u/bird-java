package com.bird.websocket.common.configuration;

import com.bird.websocket.common.authorize.IAuthorizeResolver;
import com.bird.websocket.common.authorize.NullAuthorizeResolver;
import com.bird.websocket.common.message.handler.MessageHandlerFactory;
import com.bird.websocket.common.server.BasicSessionDirectory;
import com.bird.websocket.common.server.ISessionDirectory;
import com.bird.websocket.common.server.WebSocketPublisher;
import com.bird.websocket.common.server.WebSocketServer;
import com.bird.websocket.common.synchronizer.MessageSyncComposite;
import com.bird.websocket.common.synchronizer.WebSocketServerSyncComposite;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author liuxx
 * @since 2020/12/29
 */
@Configuration
@EnableConfigurationProperties(WebSocketProperties.class)
@AutoConfigureAfter({SsoClientWebSocketAutoConfiguration.class})
@Import({MessageSyncComposite.class, WebSocketServerSyncComposite.class})
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
     * 注册 默认的websocket服务端
     */
    @Bean
    public WebSocketServer webSocketServer() {
        return new WebSocketServer();
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
     * 注册 session 仓库字典
     */
    @Bean
    public ISessionDirectory sessionDirectory(IAuthorizeResolver authorizeResolver) {
        return new BasicSessionDirectory(authorizeResolver);
    }

    /**
     * 注册 消息处理器工厂
     */
    @Bean
    public MessageHandlerFactory messageHandlerFactory(MessageSyncComposite messageSyncComposite, ISessionDirectory sessionDirectory) {
        return new MessageHandlerFactory(messageSyncComposite, sessionDirectory);
    }

    /**
     * 注册 websocket消息发送者
     */
    @Bean
    public WebSocketPublisher webSocketPublisher(MessageHandlerFactory messageHandlerFactory) {
        return new WebSocketPublisher(messageHandlerFactory);
    }
}
