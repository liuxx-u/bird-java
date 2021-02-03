package com.bird.service.common.grid.define;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author liuxx
 * @since 2021/2/3
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class StringPureAutoGrid {

    private String id;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
