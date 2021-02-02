package com.bird.service.common.grid.executor;

import com.bird.service.common.grid.executor.jdbc.JdbcGridExecutor;
import org.springframework.beans.factory.ObjectProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/2
 */
public class DefaultGridExecutorLoader implements IGridExecutorLoader {

    private final JdbcGridExecutor jdbcGridExecutor;

    public DefaultGridExecutorLoader(ObjectProvider<JdbcGridExecutor> jdbcGridExecutor) {
        this.jdbcGridExecutor = jdbcGridExecutor.getIfAvailable();
    }

    /**
     * 加载Grid方法执行器集合
     *
     * @return Grid方法执行器映射Map
     */
    @Override
    public Map<DialectType, IGridExecutor> loadExecutors() {
        Map<DialectType, IGridExecutor> map = new HashMap<>(8);
        map.put(DialectType.MYSQL, jdbcGridExecutor);
        map.put(DialectType.ORACLE, jdbcGridExecutor);

        return map;
    }
}
