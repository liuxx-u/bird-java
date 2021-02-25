package com.bird.eventbus.arg;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuxx
 */
public interface IEventArg extends Serializable {

    /**
     * 事件唯一标识
     */
    String getEventId();

    /**
     * 事件发布时间
     */
    Date getEventTime();

    /**
     * 是否本地事件
     */
    default Boolean isLocal() {
        return false;
    }
}
