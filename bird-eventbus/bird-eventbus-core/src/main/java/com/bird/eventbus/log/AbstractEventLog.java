package com.bird.eventbus.log;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Data
public abstract class AbstractEventLog implements Serializable {
    /**
     * 事件id
     */
    private String eventId;
    /**
     * 事件名称
     */
    private String event;
    /**
     * 事件json数据
     */
    private String eventJson;
}
