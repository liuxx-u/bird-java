package com.bird.service.common.grid.define;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bird.service.common.grid.annotation.GridField;
import com.bird.service.common.grid.enums.QueryStrategyEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author liuxx
 * @since 2021/2/3
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class StringAutoGrid extends StringPureAutoGrid {

    @TableLogic
    @GridField(queryStrategy = QueryStrategyEnum.HIDE)
    private Boolean delFlag;

    private Date createdAt;

    @GridField(queryStrategy = QueryStrategyEnum.HIDE)
    private Date updatedAt;
}
