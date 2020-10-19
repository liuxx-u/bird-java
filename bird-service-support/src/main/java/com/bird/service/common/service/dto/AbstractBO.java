package com.bird.service.common.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 *
 * @author liuxx
 * @date 2017/8/3
 */

@SuppressWarnings("serial")
public abstract class AbstractBO implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
