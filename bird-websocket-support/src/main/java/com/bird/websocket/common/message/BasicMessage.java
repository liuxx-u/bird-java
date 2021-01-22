package com.bird.websocket.common.message;

import lombok.Data;
import lombok.experimental.Accessors;

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
}
