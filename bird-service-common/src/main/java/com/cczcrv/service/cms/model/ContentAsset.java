package com.bird.service.cms.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.core.model.AbstractModel;

/**
 * Created by jack on 2017/7/5.
 */
@TableName("Content_Asset")
public class ContentAsset extends AbstractModel {

    /// <summary>
    /// 描述
    /// </summary>
    @TableField("Description")
    public String description ;
    /// <summary>
    /// 所属新闻Id
    /// </summary>
    @TableField("ContentId")
    public Long contentId ;
    @TableField("Cover")
    public String cover ;
    @TableField("Path")
    public String path ;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
