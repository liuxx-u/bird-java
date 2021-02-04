package com.bird.websocket.common.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yuanjian
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MultipartMessage extends BasicMessage {

    /**
     * 待发送的目标 token
     * <p>
     * 获取单点对象时，token优先级最高。发送失败不会向下寻找继续发送
     */
    private List<String> tokens;

    /**
     * 待发送的目标 userId
     * <p>
     * 优先级比token低，只有当tokens为空时，该字段才会起作用
     */
    private List<String> userIds;


    @Override
    public MessageTypeEnum getType() {
        return MessageTypeEnum.MULTIPART;
    }
}
