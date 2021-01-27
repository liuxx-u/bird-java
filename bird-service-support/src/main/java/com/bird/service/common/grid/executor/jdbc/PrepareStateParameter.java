package com.bird.service.common.grid.executor.jdbc;

import lombok.Data;

import java.util.List;

/**
 * @author liuxx
 * @since 2021/1/27
 */
@Data
public class PrepareStateParameter {

    /**
     * 参数化SQL
     */
    private String sql;
    /**
     * 参数集合
     */
    private List<Object> parameters;
}
