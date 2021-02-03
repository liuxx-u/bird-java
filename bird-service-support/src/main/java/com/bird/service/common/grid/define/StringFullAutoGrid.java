package com.bird.service.common.grid.define;

import com.bird.service.common.grid.annotation.GridField;
import com.bird.service.common.grid.enums.QueryStrategyEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @since 2021/2/3
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class StringFullAutoGrid extends StringAutoGrid {

    @GridField(queryStrategy = QueryStrategyEnum.HIDE)
    private String creatorId;

    @GridField(queryStrategy = QueryStrategyEnum.HIDE)
    private String modifierId;
}
