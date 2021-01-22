package com.bird.websocket.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yuanjian
 */
@Getter
@RequiredArgsConstructor
public enum MessageTypeEnum {

    /** 单点消息 */
    SINGLE_POINT,

    /** 多点消息 */
    MULTIPART,

    /** 广播消息 */
    BROADCAST
}
