package com.bird.service.common.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liuxx
 * @since 2020/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class StringFullDO extends StringDO implements IHasCreatorId<String>,IHasModifierId<String> {

    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifierId;
}
