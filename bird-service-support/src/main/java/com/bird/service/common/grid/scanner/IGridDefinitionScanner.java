package com.bird.service.common.grid.scanner;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/18
 */
public interface IGridDefinitionScanner {

    /**
     * 通过包名扫描表格定义{@link com.bird.service.common.grid.annotation.AutoGrid}信息
     *
     * @param packagePatterns 包名
     * @return 表格定义数据
     */
    Map<String, Class<?>> scan(String... packagePatterns);
}
