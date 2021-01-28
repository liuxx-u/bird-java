package com.bird.service.common.grid;

import com.bird.service.common.grid.definition.TestGrid;
import com.bird.service.common.grid.enums.SortDirectionEnum;
import com.bird.service.common.grid.executor.jdbc.PrepareStateParameter;
import com.bird.service.common.grid.executor.jdbc.mysql.GridMySqlParser;
import com.bird.service.common.grid.query.FilterRule;
import com.bird.service.common.grid.query.PagedListQuery;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author liuxx
 * @since 2021/1/28
 */
public class GridMySqlParserTests {

    private PagedListQuery generateQuery(){
        PagedListQuery query = new PagedListQuery();

        query.setPageIndex(2);
        query.setPageSize(10);
        query.setSortField("name");
        query.setSortDirection(SortDirectionEnum.DESC.getCode());
        query.addFilter(new FilterRule("name","liuxx"));
        query.addFilter(new FilterRule("age","less","30"));
        query.addFilter(new FilterRule("money","123"));
        return query;
    }

    @Test
    public void gridParseTest(){
        GridDefinition gridDefinition = GridDefinition.parse(TestGrid.class);
        PagedListQuery query = this.generateQuery();

        GridMySqlParser parser = new GridMySqlParser();
        PrepareStateParameter stateParameter = parser.listPaged(gridDefinition,query);

        String sql = "select `content` AS content,`money` AS money,`name` AS name,`id` AS id,`age2` AS age from table where (delFlag = 0) and (`name` = ? and `age2` < ?) order by `name` desc limit 10,10";
        Assert.assertEquals(stateParameter.getSql(),sql);
        Assert.assertEquals(stateParameter.getParameters().get(0).getParameter(),"liuxx");
        Assert.assertEquals(stateParameter.getParameters().get(1).getParameter(),"30");
    }
}
