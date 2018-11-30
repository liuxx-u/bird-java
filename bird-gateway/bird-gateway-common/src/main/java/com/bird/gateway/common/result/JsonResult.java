package com.bird.gateway.common.result;

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

    /**
     * Instantiates a new Soul result.
     */
    public JsonResult() {

    }

    /**
     * Instantiates a new Soul result.
     *
     * @param code    the code
     * @param message the message
     * @param result    the data
     */
    public JsonResult(final Integer code, final String message, final Object result) {

        this.code = code;
        this.message = message;
        this.result = result;
    }

    /**
     * return success.
     *
     * @return {@linkplain JsonResult}
     */
    public static JsonResult success() {
        return success("");
    }

    /**
     * return success.
     *
     * @param msg msg
     * @return {@linkplain JsonResult}
     */
    public static JsonResult success(final String msg) {
        return success(msg, null);
    }

    /**
     * return success.
     *
     * @param result this is result data.
     * @return {@linkplain JsonResult}
     */
    public static JsonResult success(final Object result) {
        return success(null, result);
    }

    /**
     * return success.
     *
     * @param msg  this ext msg.
     * @param result this is result data.
     * @return {@linkplain JsonResult}
     */
    public static JsonResult success(final String msg, final Object result) {
        return get(CommonErrorCode.SUCCESSFUL, msg, result);
    }

    /**
     * return error .
     *
     * @param msg error msg
     * @return {@linkplain JsonResult}
     */
    public static JsonResult error(final String msg) {
        return error(CommonErrorCode.ERROR, msg);
    }

    /**
     * return error .
     *
     * @param code error code
     * @param msg  error msg
     * @return {@linkplain JsonResult}
     */
    public static JsonResult error(final int code, final String msg) {
        return get(code, msg, null);
    }


    /**
     * return timeout .
     *
     * @param msg error msg
     * @return {@linkplain JsonResult}
     */
    public static JsonResult timeout(final String msg) {
        return error(msg);
    }

    private static JsonResult get(final int code, final String msg, final Object result) {
        return new JsonResult(code, msg, result);
    }
}
