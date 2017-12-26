package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.model.AbstractModel;

@TableName("cms_content")
public class CmsContent extends AbstractModel {
    private String title;
    private Long classifyId;
    private String brief;
    private String content;
    private String link;
    private String cover;
    private Integer orderNo;
    private Integer browserNo;
    private Integer praiseNo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getBrowserNo() {
        return browserNo;
    }

    public void setBrowserNo(Integer browserNo) {
        this.browserNo = browserNo;
    }

    public Integer getPraiseNo() {
        return praiseNo;
    }

    public void setPraiseNo(Integer praiseNo) {
        this.praiseNo = praiseNo;
    }
}
