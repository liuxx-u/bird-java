package com.bird.service.boot.starter.eventbus.kafka;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2018/3/23
 */
@Getter
@Setter
public class KafkaListenerProperties {
    private String groupId;
    private String basePackages;
}
