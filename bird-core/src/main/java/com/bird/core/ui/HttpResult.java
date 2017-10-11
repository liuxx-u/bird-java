package com.bird.core.ui;

import com.bird.core.HttpCode;

import java.io.Serializable;

/**
 * Created by liuxx on 2017/5/12.
 */
public class HttpResult implements Serializable {
    private static final long serialVersionUID = 6288374846131788743L;

    /**
     * 信息
     */
    private String message;

    /**
     * 状态码
     */
    private HttpCode statusCode;

    /**
     * 是否成功
     */
    private boolean success;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HttpCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpCode statusCode) {
        this.statusCode = statusCode;
    }

    public HttpResult() {

    }
}
