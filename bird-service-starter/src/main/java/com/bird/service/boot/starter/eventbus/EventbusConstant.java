package com.bird.service.boot.starter.eventbus;

/**
 * @author liuxx
 * @date 2018/3/23
 */
public interface EventbusConstant {
    String PREFIX = "eventbus";

    interface KAFKA {
        String PREFIX = EventbusConstant.PREFIX + ".kafka";
    }
}
