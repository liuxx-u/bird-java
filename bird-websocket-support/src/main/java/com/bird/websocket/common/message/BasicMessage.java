package com.bird.websocket.common.message;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 基础消息
 *
 * @author yuanjian
 */
@Data
@Accessors(chain = true)
public abstract class BasicMessage implements Message {

    /** 是否异步发送消息 */
    private boolean isAsync;
    /** 消息体 */
    private String content;
    /** 是否开启延时缓存功能 */
    private boolean isDelay;
    /** 自定义延时缓存时间 */
    private long delayDuration;
    /** 业务key */
    private Map<String, String> item;

    @Override
    public String getItem(String key) {
        return getItem().get(key);
    }

    public void addItem(String key, String value) {
        getItem().put(key, value);
    }

    public Map<String, String> getItem() {
        if (item == null) {
            item = Maps.newConcurrentMap();
        }
        return item;
    }
}
