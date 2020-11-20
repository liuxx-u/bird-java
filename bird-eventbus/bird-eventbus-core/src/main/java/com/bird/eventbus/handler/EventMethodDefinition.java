package com.bird.eventbus.handler;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2019/1/16
 */
@Data
public class EventMethodDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件名称
     */
    private String event;
    /**
     * 所属消费者组
     */
    private String group;
    /**
     * 监听类名称
     */
    private String clazz;
    /**
     * 事件监听方法
     */
    private String method;
}
