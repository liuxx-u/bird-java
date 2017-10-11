package com.bird.core.http;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by liuxx on 2017/7/24.
 */
public class AbpViewModel {
    @JSONField(name="__abp")
    private Boolean abp;
    private Boolean success;
    private Object result;


    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }



    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getAbp() {
        return abp;
    }

    public void setAbp(Boolean abp) {
        this.abp = abp;
    }
}
