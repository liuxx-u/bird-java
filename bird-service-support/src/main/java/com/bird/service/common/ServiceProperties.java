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

    /**
     * 是否开启 乐观锁检测
     * 支持的数据类型只有:int,Integer,long,Long,Date,Timestamp,LocalDateTime
     * 整数类型下 newVersion = oldVersion + 1, newVersion 会回写到 entity 中
     * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
     * 在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
     */
    private Boolean optimisticLockCheck;
}
