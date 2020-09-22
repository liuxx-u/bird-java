package com.bird.service.common.model;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author liuxx
 * @date 2019/8/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class LongDO extends LongPureDO implements ISoftDelete,IHasCreateTime,IHasModifyTime {

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean delFlag;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifiedTime;
}
