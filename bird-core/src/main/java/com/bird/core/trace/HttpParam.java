package com.bird.core.trace;

import lombok.Data;

import java.util.Map;

/**
 * @author liuxx
 * @since 2020/10/10
 */
@Data
public class HttpParam {

    /**
     * 请求头
     */
    private Map<String,String> headers;
    /**
     * 请求参数
     */
    private String params;
    /**
     * Body内容
     */
    private String body;
}
