package com.bird.web.sso.event;

import com.google.common.eventbus.Subscribe;

/**
 * 事件监听者
 *
 * @author liuxx
 * @date 2019/3/7
 */
public interface ISsoEventListener<T extends SsoEvent> {

    /**
     * 监听事件
     * @param event 事件
     */
    @Subscribe
    void onEvent(T event);
}
