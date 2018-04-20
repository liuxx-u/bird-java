package com.bird.service.boot.starter.eventbus;

/**
 * @author liuxx
 * @date 2018/3/23
 */
public interface EventbusConstant {
    /**
     * eventbus 配置前缀
     */
    String PREFIX = "eventbus";

    interface Kafka {
        /**
         * kafka 配置前缀
         */
        String PREFIX = EventbusConstant.PREFIX + ".kafka";

        /**
         * kafka broker地址
         */
        String HOST_PROPERTY_NAME = PREFIX + ".host";

        /**
         * kafka 提供者默认的topic
         */
        String PROVIDER_DEFAULT_TOPIC_PROPERTY_NAME = PREFIX + ".provider.defaultTopic";

        /**
         * kafka 消费者的groupId
         */
        String LISTENER_GROUP_ID = PREFIX + ".listener.groupId";
    }
}
