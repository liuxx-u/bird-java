package com.cczcrv.core.controller;

import com.cczcrv.core.HttpCode;

/**
 * Created by liuxx on 2017/7/13.
 */
public class OperationResult<T extends Object> {
    private int httpCode;
    private String message;
    private Long timestamp;
    private T result;

    public static <T> OperationResult<T> Success(String message,T result){
        return new OperationResult<>(message,result);
    }

    public static OperationResult Fail(int httpCode,String message) {
        return new OperationResult(httpCode, message);
    }

    public OperationResult() {
        timestamp = System.currentTimeMillis();
    }

    public OperationResult(String message,T result) {
        this(HttpCode.OK.value(), message, result);
    }

    public OperationResult(int httpCode,String message) {
        this(httpCode,message,null);
    }

    public OperationResult(int httpCode,String message,T result) {
        this();
        this.httpCode = httpCode;
        this.message = message;
        this.result = result;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
