package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.executor.IGridExecutor;
import com.bird.service.common.grid.query.PagedListQuery;
import com.bird.service.common.grid.query.PagedResult;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/20
 */
public class JdbcGridExecutor implements IGridExecutor {

    private final IDataSourceChooser dataSourceSelector;

    public JdbcGridExecutor(IDataSourceChooser dataSourceSelector){
        this.dataSourceSelector = dataSourceSelector;
    }

    private Connection connection() {
        DataSource dataSource = this.dataSourceSelector.gridDataSource();
        if (dataSource == null) {
            throw new RuntimeException("dataSource is null");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }

    /**
     * 分页查询
     *
     * @param gridClass 表格定义类
     * @param query     分页查询参数
     * @return 查询结果
     */
    @Override
    public PagedResult<Map<String, Object>> listPaged(Class<?> gridClass, PagedListQuery query) {
        Connection connection = connection();
        try {
            PreparedStatement statement = connection.prepareStatement("");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 新增
     *
     * @param gridClass 表格定义类
     * @param pojo      新增数据
     * @return 主键
     */
    @Override
    public Object add(Class<?> gridClass, Map<String, Object> pojo) {
        return null;
    }

    /**
     * 编辑
     *
     * @param gridClass 表格定义类
     * @param pojo      更新的数据
     * @return 主键
     */
    @Override
    public Object edit(Class<?> gridClass, Map<String, Object> pojo) {
        return null;
    }

    /**
     * 删除
     *
     * @param gridClass 表格定义类
     * @param id        主键
     * @return 是否删除成功
     */
    @Override
    public boolean delete(Class<?> gridClass, Object id) {
        return false;
    }
}
