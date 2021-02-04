package com.bird.websocket.common.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yuanjian
 */
@Getter
@RequiredArgsConstructor
public enum StorageTypeEnum {

    /**
     * 内部存储
     */
    INTERNAL,

    /**
     * redis存储
     */
    REDIS
}
