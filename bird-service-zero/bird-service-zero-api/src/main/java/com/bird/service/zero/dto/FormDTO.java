package com.bird.service.zero.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.service.dto.EntityDTO;

@TableName("zero_form")
public class FormDTO extends EntityDTO {
    private String name;
    private String key;
    private String description;
    private boolean enabled;
    private boolean withTab;
    private String saveUrl;
    private String tabType;
    private String tabPosition;
    private String defaultGroupName;
    private Integer lineCapacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isWithTab() {
        return withTab;
    }

    public void setWithTab(boolean withTab) {
        this.withTab = withTab;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public String getTabPosition() {
        return tabPosition;
    }

    public void setTabPosition(String tabPosition) {
        this.tabPosition = tabPosition;
    }

    public String getDefaultGroupName() {
        return defaultGroupName;
    }

    public void setDefaultGroupName(String defaultGroupName) {
        this.defaultGroupName = defaultGroupName;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public Integer getLineCapacity() {
        return lineCapacity;
    }

    public void setLineCapacity(Integer lineCapacity) {
        this.lineCapacity = lineCapacity;
    }
}
