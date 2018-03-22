package com.bird.service.zero.dto;

import com.bird.service.common.service.dto.EntityDTO;

import java.util.ArrayList;
import java.util.List;

public class FormOperateDTO extends EntityDTO {
    private boolean withTab;
    private String saveUrl;
    private String tabType;
    private String tabPosition;
    private String defaultGroupName;
    private int lineCapacity;
    private List<FieldDTO> fields;

    public FormOperateDTO(){
        fields = new ArrayList<>();
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

    public List<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<FieldDTO> fields) {
        this.fields = fields;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public int getLineCapacity() {
        return lineCapacity;
    }

    public void setLineCapacity(int lineCapacity) {
        this.lineCapacity = lineCapacity;
    }
}
