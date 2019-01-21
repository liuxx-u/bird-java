package com.bird.service.boot.starter.eventbus.rabbit;

import lombok.Getter;
import lombok.Setter;
import com.bird.service.boot.starter.eventbus.EventbusConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @date 2019/1/18
 */
@Getter
@Setter
@ConfigurationProperties(prefix = EventbusConstant.Rabbit.PREFIX)
public class RabbitProperties {
    private String address;
    private String user;
    private String password;
    private String virtualHost = "/";
    private String listenerPackages;
}
