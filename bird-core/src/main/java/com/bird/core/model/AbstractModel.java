package com.bird.core.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liuxx on 2017/5/15.
 */
@SuppressWarnings("serial")
public abstract class AbstractModel implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("delFlag")
    private Integer delFlag;
    @TableField("deleteBy")
    private Long deleteBy;
    @TableField("deleteTime")
    private Date deleteTime;
    @TableField("createBy")
    private Long createBy;
    @TableField("createTime")
    private Date createTime;
    @TableField("remark")
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }


    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(Long deleteBy) {
        this.deleteBy = deleteBy;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
}
