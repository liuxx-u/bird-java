package com.bird.gateway.common.dto;

import com.bird.gateway.common.exception.CommonErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/11/28
 */
@Data
public class JsonResult implements Serializable {

    private static final long serialVersionUID = -2792556188993845048L;

    private Integer code;

    private String message;

    private Object result;

    public JsonResult() {

    }

    public JsonResult(final Integer code, final String message, final Object result) {

        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static JsonResult success() {
        return success("");
    }

    public static JsonResult success(final String msg) {
        return success(msg, null);
    }

    public static JsonResult success(final Object result) {
        return success(null, result);
    }

    public static JsonResult success(final String msg, final Object result) {
        return get(CommonErrorCode.SUCCESSFUL, msg, result);
    }

    public static JsonResult error(final String msg) {
        return error(CommonErrorCode.ERROR, msg);
    }

    public static JsonResult error(final int code, final String msg) {
        return get(code, msg, null);
    }

    public static JsonResult timeout(final String msg) {
        return error(msg);
    }

    private static JsonResult get(final int code, final String msg, final Object result) {
        return new JsonResult(code, msg, result);
    }
}
