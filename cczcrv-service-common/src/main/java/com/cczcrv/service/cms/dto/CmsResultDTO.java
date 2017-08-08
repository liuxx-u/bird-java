package com.cczcrv.service.cms.dto;

import com.cczcrv.core.service.SerializableDTO;
import com.cczcrv.service.cms.model.Content;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuxx on 2017/6/27.
 */
public class CmsResultDTO extends SerializableDTO {
    private Long id;
    private String title;
    private String cover;
    private String link;
    private int browseNo;
    private int praiseNo;

    private Map<String,String> attribute;

    public CmsResultDTO() {
        attribute = new HashMap<>();
    }

    public CmsResultDTO(Content content) {
        id = content.getId();
        title = content.getTitle();
        cover = content.getCover();
        link = content.getLink();
        browseNo = content.getBrowseNo();
        praiseNo = content.getPraiseNo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Map<String, String> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, String> attribute) {
        this.attribute = attribute;
    }
}
