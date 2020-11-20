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

    interface Handler {
        /**
         * Handler 配置前缀
         */
        String PREFIX = EventbusConstant.PREFIX + ".handler";
        /**
         * 事件处理方法 包扫码路径
         */
        String SCAN_PACKAGES = PREFIX + ".scan-packages";
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

    }
}
