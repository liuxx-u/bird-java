package com.bird.eventbus.kafka.configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2018/3/23
 */
@Getter
@Setter
public class KafkaProviderProperties {
    private String defaultTopic;
    private Integer retries;
    private Integer batchSize;
    private Integer lingerms;
    private Long bufferMemory;
}
