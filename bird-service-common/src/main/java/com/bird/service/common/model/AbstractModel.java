package com.bird.service.common.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;

/**
 * @author liuxx
 * @date 2018/3/19
 *
 */
public abstract class AbstractModel extends AbstractPureModel implements ISoftDelete,IHasCreateTime,IHasModifyTime {
    @TableLogic
    private Boolean delFlag;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Date modifiedTime;

    @Override
    public Boolean getDelFlag() {
        return delFlag;
    }

    @Override
    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getModifiedTime() {
        return modifiedTime;
    }

    @Override
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
