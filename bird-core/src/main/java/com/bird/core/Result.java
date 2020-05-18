package com.bird.core;

import com.bird.core.exception.AbstractException;
import com.bird.core.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxx
 * Created by liuxx on 2017/7/13.
 */
@Data
public class Result<T> implements Serializable {
    private String code;
    private String message;
    private Long timestamp;
    private T data;

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(message, data);
    }

    public static Result fail(String errorCode, String message) {
        return new Result(errorCode, message);
    }

    public static Result fail(AbstractException exception) {
        String message = exception.getMessage();

        return new Result(exception.getErrorCode(), message);
    }

    public Result() {
        timestamp = System.currentTimeMillis();
    }

    public Result(String message, T result) {
        this(ErrorCode.SUCCESS.getCode(), message, result);
    }

    public Result(String code, String message) {
        this(code, message, null);
    }

    public Result(String code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
