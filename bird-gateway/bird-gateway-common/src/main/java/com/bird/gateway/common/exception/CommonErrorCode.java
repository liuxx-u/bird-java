package com.bird.gateway.common.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @date 2018/11/28
 */
public class CommonErrorCode {

    /**
     * The constant ERROR.
     */
    public static final int ERROR = -2;

    /**
     * The constant SUCCESSFUL.
     */
    public static final int SUCCESSFUL = 200;

    public static final int UNAUTHORIZED = 401;

    /**
     * getErrorMsg.
     *
     * @param code code
     * @return msg error msg
     */
    public static String getErrorMsg(final int code) {
        String msg = System.getProperty(String.valueOf(code));
        if (StringUtils.isBlank(msg)) {
            return " code [" + code + "] not match msg.";
        }
        return msg;
    }
}
