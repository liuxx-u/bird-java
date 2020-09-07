package com.bird.web.common.security.ip;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @since 2020/9/4
 */
@Data
public class IpCheckProperties {

    /**
     * 是否启用ip白名单校验
     */
    private Boolean enable = false;
    /**
     * ip白名单配置集合
     */
    @NestedConfigurationProperty
    private List<IpConfProperties> ipList = new ArrayList<>();
}
