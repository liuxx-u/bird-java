package com.cczcrv.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cczcrv.core.model.AbstractModel;

import java.util.Date;

/**
 * Created by panda on 2017/6/6.
 */
@TableName("Cms_Content")
public class Content extends AbstractModel {

    @TableField("Title")
    private String title;


    @TableField("contents")
    private String contents;


    @TableField("Cover")
    private String cover;


    @TableField("Link")
    private String link;


    @TableField("OrderNo")
    private int orderNo;


    @TableField("BrowseNo")
    private int browseNo;


    @TableField("PraiseNo")
    private int praiseNo;


    @TableField("IsPublish")
    private Boolean isPublish;

    @TableField("ClassifyId")
    private int classifyId;


    @TableField("DeleterUserId")
    private long deleterUserId;

    @TableField("DeletionTime")
    private Date deletionTime;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getBrowseNo() {
        return browseNo;
    }

    public void setBrowseNo(int browseNo) {
        this.browseNo = browseNo;
    }

    public int getPraiseNo() {
        return praiseNo;
    }

    public void setPraiseNo(int praiseNo) {
        this.praiseNo = praiseNo;
    }

    public Boolean getPublish() {
        return isPublish;
    }

    public void setPublish(Boolean publish) {
        isPublish = publish;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public long getDeleterUserId() {
        return deleterUserId;
    }

    public void setDeleterUserId(long deleterUserId) {
        this.deleterUserId = deleterUserId;
    }

    public Date getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(Date deletionTime) {
        this.deletionTime = deletionTime;
    }

}
