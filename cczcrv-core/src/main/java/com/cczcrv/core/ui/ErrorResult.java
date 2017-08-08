package com.cczcrv.core.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuxx on 2017/5/12.
 */
public class ErrorResult extends HttpResult {
    private static final long serialVersionUID = 8567221653356186674L;

    /**
     * 封装多个 错误信息
     */
    private Map<String, Object> errors = new HashMap<String,Object>();

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }

    public ErrorResult() {

    }
}
