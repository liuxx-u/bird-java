package com.bird.service.common.grid.definition;

import com.bird.service.common.grid.annotation.AutoGrid;
import com.bird.service.common.grid.annotation.GridField;
import com.bird.service.common.grid.enums.QueryStrategyEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuxx
 * @since 2021/1/28
 */
@Data
@AutoGrid(name = "test",from = "table",where = "delFlag = 0")
public class TestGrid {

    private String id;

    private String name;

    @GridField(dbField = "content")
    private String content;

    @GridField(dbField = "age2")
    private Integer age;

    @GridField(queryStrategy = QueryStrategyEnum.FORBID)
    private BigDecimal money;
}
