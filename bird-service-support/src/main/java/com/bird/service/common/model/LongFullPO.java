package com.bird.service.common.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

/**
 * @author liuxx
 * @since 2020/10/13
 */
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public abstract class LongFullPO extends LongPO implements IHasCreatedBy, IHasUpdatedBy {

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;
}
