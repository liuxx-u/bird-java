package com.bird.core;

import com.bird.core.exception.AbstractException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author liuxx
 * Created by liuxx on 2017/7/13.
 */
@Getter
@Setter
public class OperationResult<T extends Object> implements Serializable {
    private int code;
    private String message;
    private Long timestamp;
    private T result;

    public static <T> OperationResult<T> Success(String message, T result) {
        return new OperationResult<>(message, result);
    }

    public static OperationResult Fail(int httpCode, String message) {
        return new OperationResult(httpCode, message);
    }

    public static OperationResult Fail(AbstractException exception) {
        String message = exception.getMessage();

        return new OperationResult(exception.getCode(), message);
    }

    public OperationResult() {
        timestamp = System.currentTimeMillis();
    }

    public OperationResult(String message, T result) {
        this(HttpCode.OK.value(), message, result);
    }

    public OperationResult(int httpCode, String message) {
        this(httpCode, message, null);
    }

    public OperationResult(int httpCode, String message, T result) {
        this();
        this.code = httpCode;
        this.message = message;
        this.result = result;
    }
}
