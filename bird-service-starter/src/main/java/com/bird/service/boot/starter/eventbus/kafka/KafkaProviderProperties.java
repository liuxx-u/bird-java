package com.bird.service.boot.starter.eventbus.kafka;

/**
 * @author liuxx
 * @date 2018/3/23
 */
public class KafkaProviderProperties {
    private String defaultTopic;
    private Integer retries;
    private Integer batchSize;
    private Integer lingerms;
    private Long bufferMemory;

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getLingerms() {
        return lingerms;
    }

    public void setLingerms(Integer lingerms) {
        this.lingerms = lingerms;
    }

    public Long getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(Long bufferMemory) {
        this.bufferMemory = bufferMemory;
    }
}
