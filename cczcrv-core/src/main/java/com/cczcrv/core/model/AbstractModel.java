package com.cczcrv.core.model;

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
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;
    @TableField("IsDeleted")
    private Integer enable;
    @TableField("CreatorUserId")
    private Long createBy;
    @TableField("CreationTime")
    private Date createTime;
    @TableField("LastModifierUserId")
    private Long updateBy;
    @TableField("LastModificationTime")
    private Date updateTime;

    @TableField(exist = false)
    private String keyword;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the createBy
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * @param createBy
     *            the createBy to set
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the updateBy
     */
    public Long getUpdateBy() {
        return updateBy;
    }

    /**
     * @param updateBy
     *            the updateBy to set
     */
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     *            the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
