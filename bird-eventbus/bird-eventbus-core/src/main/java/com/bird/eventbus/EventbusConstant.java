package com.bird.eventbus;

/**
 * @author liuxx
 * @date 2018/3/23
 */
public interface EventbusConstant {
    /**
     * eventbus 配置前缀
     */
    String PREFIX = "bird.eventbus";

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
         * kafka 扫描消费者 路径
         */
        String LISTENER_PACKAGES = PREFIX + ".listener.base-packages";
    }

    interface Rabbit {
        /**
         * rabbit 配置前缀
         */
        String PREFIX = EventbusConstant.PREFIX + ".rabbit";

        /**
         * kafka broker地址
         */
        String ADDRESS_PROPERTY_NAME = PREFIX + ".address";

        /**
         * kafka 扫描消费者 路径
         */
        String LISTENER_PACKAGES = PREFIX + ".listener-packages";

    }
}
