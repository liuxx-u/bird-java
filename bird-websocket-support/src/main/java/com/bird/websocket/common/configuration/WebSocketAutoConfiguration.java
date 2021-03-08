package com.bird.websocket.common.configuration;

import com.bird.websocket.common.authorize.IAuthorizeResolver;
import com.bird.websocket.common.authorize.NullAuthorizeResolver;
import com.bird.websocket.common.delay.DelayMessageInterceptor;
import com.bird.websocket.common.delay.DelayWebSocketServerInterceptor;
import com.bird.websocket.common.delay.IDelayMessageStorage;
import com.bird.websocket.common.delay.MemoryDelayMessageStorage;
import com.bird.websocket.common.interceptor.MessageInterceptor;
import com.bird.websocket.common.interceptor.MessageInterceptorComposite;
import com.bird.websocket.common.interceptor.WebSocketServerInterceptor;
import com.bird.websocket.common.interceptor.WebSocketServerInterceptorComposite;
import com.bird.websocket.common.interceptor.log.LogMessageInterceptor;
import com.bird.websocket.common.interceptor.log.LogWebSocketServerInterceptor;
import com.bird.websocket.common.message.handler.MessageHandlerFactory;
import com.bird.websocket.common.server.BasicSessionDirectory;
import com.bird.websocket.common.server.ISessionDirectory;
import com.bird.websocket.common.server.WebSocketPublisher;
import com.bird.websocket.common.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@Import({LogMessageInterceptor.class, LogWebSocketServerInterceptor.class, MessageInterceptorComposite.class, WebSocketServerInterceptorComposite.class})
public class WebSocketAutoConfiguration {

    @Autowired
    private WebSocketProperties webSocketProperties;

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
    public MessageHandlerFactory messageHandlerFactory(MessageInterceptorComposite messageInterceptorComposite, ISessionDirectory sessionDirectory) {
        return new MessageHandlerFactory(messageInterceptorComposite, sessionDirectory);
    }

    /**
     * 注册 websocket消息发送者
     */
    @Bean
    public WebSocketPublisher webSocketPublisher(MessageHandlerFactory messageHandlerFactory) {
        return new WebSocketPublisher(messageHandlerFactory);
    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(DelayProperties.class)
    @ConditionalOnProperty(prefix = "bird.websocket.delay", name = "enable", havingValue = "true", matchIfMissing = true)
    static class Delay {

        @Bean
        @ConditionalOnMissingBean(IDelayMessageStorage.class)
        public IDelayMessageStorage delayMessageStorage(DelayProperties delayProperties) {
            MemoryDelayMessageStorage delayMessageStorage = new MemoryDelayMessageStorage(delayProperties.getDuration());
            delayMessageStorage.schedulePrune(delayProperties.getClearInterval());
            return delayMessageStorage;
        }

        @Bean
        public MessageInterceptor delayMessageInterceptor(IDelayMessageStorage delayMessageStorage, DelayProperties delayProperties) {
            return new DelayMessageInterceptor(delayMessageStorage, delayProperties.getDuration());
        }

        @Bean
        public WebSocketServerInterceptor webSocketServerInterceptor(IDelayMessageStorage delayMessageStorage, ISessionDirectory sessionDirectory) {
            return new DelayWebSocketServerInterceptor(delayMessageStorage, sessionDirectory);
        }
    }

}
