package com.cczcrv.core.ui;

/**
 * Created by liuxx on 2017/5/12.
 */
public class JsonResult<T> extends HttpResult {
    private static final long serialVersionUID = 7880907731807860636L;

    /**
     * 数据
     */
    private T data;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public JsonResult() {
        super();
    }

    /**
     * 自定义返回的结果
     *
     * @param data
     * @param message
     * @param success
     */
    public JsonResult(T data, String message, boolean success) {
        this.data = data;
        super.setMessage(message);
        super.setSuccess(success);
    }

    /**
     * 成功返回数据和消息
     *
     * @param data
     * @param message
     */
    public JsonResult(T data, String message) {
        this.data = data;
        super.setMessage(message);
        super.setSuccess(true);
    }

    /**
     * 成功返回数据
     *
     * @param data
     */
    public JsonResult(T data) {
        this.data = data;
        super.setSuccess(true);
    }
}
