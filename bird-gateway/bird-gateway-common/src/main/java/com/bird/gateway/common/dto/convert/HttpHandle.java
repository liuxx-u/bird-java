package com.bird.gateway.common.dto.convert;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/12/19
 */
@Getter
@Setter
public class HttpHandle extends HystrixHandle implements Serializable {
    /**
     * url
     */
    private String url;
}
