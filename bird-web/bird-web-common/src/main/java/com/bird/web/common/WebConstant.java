package com.bird.web.common;

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
        String IDEMPOTENCY_NAMESPACE = "bird:operation_token:";
    }
}
