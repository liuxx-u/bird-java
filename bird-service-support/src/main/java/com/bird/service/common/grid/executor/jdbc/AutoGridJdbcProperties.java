package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.enums.DbFieldMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2021/2/3
 */
@Data
@ConfigurationProperties(prefix = "bird.service.grid.jdbc")
public class AutoGridJdbcProperties {

    /**
     * 字段命名规范，默认：小驼峰
     */
    private DbFieldMode dbFieldMode = DbFieldMode.SAME;
    /**
     * 逻辑删除全局值（默认 1、表示已删除）
     */
    private Object logicDeleteValue = 1;
    /**
     * 逻辑未删除全局值（默认 0、表示未删除）
     */
    private Object logicNotDeleteValue = 0;
}
