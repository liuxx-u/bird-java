package com.bird.gateway.common.dto.convert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/11/29
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class DubboHandle extends HystrixHandle implements Serializable {
    /**
     * zookeeper url is required.
     */
    private String registry;

    /**
     * dubbo application name is required.
     */
    private String appName;

    /**
     * dubbo protocol.
     */
    private String protocol;

    /**
     * port.
     */
    private int port;

    /**
     *  group.
     */
    private String group;

    /**
     * retries.
     */
    private Integer retries;

    /**
     * timeout
     */
    private Integer timeout;

    /**
     * {@linkplain com.bird.gateway.common.enums.LoadBalanceEnum}
     */
    private String loadBalance;

    /**
     * interfaceName
     */
    private String interfaceName;

    /**
     * methodName
     */
    private String methodName;

    /**
     * parameter name and type json
     */
    private String parameters;

    /**
     * version
     */
    private String version;
}
