package com.bird.service.common.grid;

import com.bird.service.common.grid.definition.TestGrid;
import com.bird.service.common.grid.enums.SortDirectionEnum;
import com.bird.service.common.grid.executor.jdbc.AutoGridJdbcProperties;
import com.bird.service.common.grid.executor.jdbc.PreparedStateParameter;
import com.bird.service.common.grid.executor.jdbc.mysql.GridMySqlParser;
import com.bird.service.common.service.query.FilterRule;
import com.bird.service.common.service.query.PagedListQuery;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/28
 */
public class GridMySqlParserTests {

    private AutoGridJdbcProperties gridJdbcProperties = new AutoGridJdbcProperties();

    private PagedListQuery generateQuery() {
        PagedListQuery query = new PagedListQuery();

        query.setPageIndex(2);
        query.setPageSize(10);
        query.setSortField("name");
        query.setSortDirection(SortDirectionEnum.DESC.getCode());
        query.addFilter(new FilterRule("name", "liuxx"));
        query.addFilter(new FilterRule("age", "less", "30"));
        query.addFilter(new FilterRule("money", "123"));
        return query;
    }

    private Map<String, Object> map() {
        Map<String, Object> pojo = new HashMap<>();
        pojo.put("id", "1111");
        pojo.put("name", "liuxx");
        pojo.put("age", 10);

        return pojo;
    }

    @Test
    public void queryTest() {
        GridDefinition gridDefinition = GridDefinition.parse(TestGrid.class);
        PagedListQuery query = this.generateQuery();

        GridMySqlParser parser = new GridMySqlParser(gridJdbcProperties);
        PreparedStateParameter stateParameter = parser.listPaged(gridDefinition, query);

        String sql = "select `id` as id,`name` as name,`content` as content,`age2` as age,`money` as money from table1 where (delFlag = 0) and (`name` = ? and `age2` < ?) order by `name` desc limit 10,10";
        Assert.assertEquals(stateParameter.getSql(), sql);
        Assert.assertEquals(stateParameter.getParameters().get(0).getParameter(), "liuxx");
        Assert.assertEquals(stateParameter.getParameters().get(1).getParameter(), "30");
    }

    @Test
    public void insertTest() {
        GridDefinition gridDefinition = GridDefinition.parse(TestGrid.class);

        Map<String, Object> pojo = map();

        GridMySqlParser parser = new GridMySqlParser(gridJdbcProperties);
        PreparedStateParameter stateParameter = parser.add(gridDefinition, pojo);
        Assert.assertEquals(stateParameter.getSql(), "insert into table1 (`id`,`name`,`content`,`age2`,`money`) values (?,?,?,?,?)");
    }

    @Test
    public void editTest() {
        GridDefinition gridDefinition = GridDefinition.parse(TestGrid.class);

        Map<String, Object> pojo = map();

        GridMySqlParser parser = new GridMySqlParser(gridJdbcProperties);
        PreparedStateParameter stateParameter = parser.edit(gridDefinition, pojo);
        Assert.assertEquals(stateParameter.getSql(), "update table1 set `name` = ?,`age2` = ? where `id` = ?");
    }

    @Test
    public void deleteTest() {
        GridDefinition gridDefinition = GridDefinition.parse(TestGrid.class);

        GridMySqlParser parser = new GridMySqlParser(gridJdbcProperties);
        PreparedStateParameter stateParameter = parser.delete(gridDefinition, 1);
        Assert.assertEquals(stateParameter.getSql(), "delete from table1 where `id` = ?");
    }
}
