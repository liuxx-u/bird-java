package com.bird.service.common.service.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author liuxx
 * @date 2017/10/16
 */
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public abstract class LongEntityBO extends EntityBO<Long> {
}
