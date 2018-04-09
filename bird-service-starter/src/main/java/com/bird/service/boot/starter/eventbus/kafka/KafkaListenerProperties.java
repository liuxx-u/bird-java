package com.bird.service.boot.starter.eventbus.kafka;

/**
 * @author liuxx
 * @date 2018/3/23
 */
public class KafkaListenerProperties {
    private String groupId;
    private String basePackages;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }
}
