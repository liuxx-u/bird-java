package com.bird.websocket.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author YJ
 */
@Data
@ConfigurationProperties(prefix = "bird.websocket.delay")
public class DelayProperties {

    /** 是否开启延时缓存发送功能 */
    private Boolean enable = true;
    /** 默认缓存时间，单位为毫秒（默认1小时） */
    private Long duration = 60 * 60 * 1000L;
    /** 清理过期，单位为毫秒（默认5分钟） */
    private Long clearInterval = 5 * 60 * 1000L;
}
