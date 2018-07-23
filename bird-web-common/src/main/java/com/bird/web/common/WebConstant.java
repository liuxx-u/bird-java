package com.bird.web.common;

import com.bird.core.Constant;

/**
 * @author liuxx
 * @date 2018/4/26
 */
public interface WebConstant {

    String UNKNOWN_IP = "unknown";

    /**
     * Part of HTTP content type header.
     */
    String MULTIPART = "multipart/";

    interface Cache {
        String IDEMPOTENCY_NAMESPACE = Constant.Cache.NAMESPACE + "operation_token:";
    }
}
