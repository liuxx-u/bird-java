package com.bird.websocket.common.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yuanjian
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SingleMessage extends BasicMessage {

    /**
     * 待发送的目标 token
     * <p>
     * 获取单点对象时，token优先级最高。发送失败会向下寻找继续发送，直到发送一个成功消息为止
     */
    private String token;

    /**
     * 待发送的目标 userId
     * <p>
     * 优先级比token低，并且发送时会通过userId寻找对应的token，若寻找到多个，会只发送一次成功的消息
     */
    private String userId;

    @Override
    public MessageTypeEnum getType() {
        return MessageTypeEnum.SINGLE_POINT;
    }
}
