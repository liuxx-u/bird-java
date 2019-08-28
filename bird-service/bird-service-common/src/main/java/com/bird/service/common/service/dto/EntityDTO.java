package com.bird.service.common.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 * @author liuxx
 * @date 2017/10/16
 */
@Getter
@Setter
@SuppressWarnings("serial")
public abstract class EntityDTO extends AbstractDTO {
    private Long id;
    private Date createTime;
}
