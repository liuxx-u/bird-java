package com.bird.eventbus.arg;

/**
 * 本地事件参数
 *
 * @author liuxx
 * @since 2021/2/22
 */
public abstract class LocalEventArg extends EventArg {

    @Override
    public Boolean isLocal() {
        return true;
    }
}
