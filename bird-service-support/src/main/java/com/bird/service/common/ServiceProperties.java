package com.bird.service.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/9/21
 */
@Data
@ConfigurationProperties(prefix = "bird.service")
public class ServiceProperties {

    /**
     * 是否开启 审计字段自动填充
     */
    private Boolean auditMetaObject = true;

    /**
     * 是否开启 防止全表更新与删除
     */
    private Boolean blockAttack = true;
}
