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
    /** 业务key */
    private Map<String, String> businessItem;

    @Override
    public String getBusinessItem(String key) {
        return getBusinessItem().get(key);
    }

    public void addBusinessItem(String key, String value) {
        getBusinessItem().put(key, value);
    }

    public Map<String, String> getBusinessItem() {
        if (businessItem == null) {
            businessItem = Maps.newHashMap();
        }
        return businessItem;
    }
}
