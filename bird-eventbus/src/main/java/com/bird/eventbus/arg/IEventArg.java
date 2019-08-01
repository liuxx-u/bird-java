package com.bird.eventbus.arg;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

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
     * 事件扩展参数
     */
    Map<String,Object> getExts();

    /**
     * 添加扩展参数
     * @param key key
     * @param value value
     */
    void addExt(String key,Object value);

    /**
     * 获取扩展参数
     * @param key key
     */
    Object getExt(String key);
}
