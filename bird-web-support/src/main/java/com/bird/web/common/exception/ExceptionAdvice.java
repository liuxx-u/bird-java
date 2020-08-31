package com.bird.web.common.exception;

import com.bird.core.Result;
import com.bird.core.exception.ErrorCode;
import com.bird.core.exception.FeignErrorException;
import com.bird.core.exception.UserArgumentException;
import com.bird.core.exception.UserFriendlyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理
 *
 * @author liuxx
 * @date 2019/5/15
 */
@Slf4j
@ControllerAdvice(basePackages = "cn.chengtay")
public class ExceptionAdvice {

    private final static String DEFAULT_ERROR_MASSAGE = "系统走神了,请稍候再试.";
    private final static String ARGUMENT_ERROR_MESSAGE = "非法的参数";

    /**
     * 校验类异常处理
     *
     * @param response response
     * @param ex       {@link MethodArgumentNotValidException}
     * @return {@link Result}
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handle(HttpServletResponse response, MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError == null ? ARGUMENT_ERROR_MESSAGE : fieldError.getDefaultMessage();

        log.warn(message, ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new Result(ErrorCode.A0001.getCode(), message);
    }

    /**
     * 校验类异常处理
     *
     * @param response response
     * @param ex       {@link ConstraintViolationException}
     * @return {@link Result}
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handle(HttpServletResponse response, ConstraintViolationException ex) {

        String message = ex.getMessage().substring(ex.getMessage().indexOf(":") + 1);

        log.warn(message, ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new Result(ErrorCode.A0001.getCode(), message);
    }

    /**
     * 非法参数异常处理
     *
     * @param response response
     * @param ex       {@link IllegalArgumentException}
     * @return {@link Result}
     */
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handle(HttpServletResponse response, IllegalArgumentException ex) {

        log.warn(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new Result(ErrorCode.A0001.getCode(), ARGUMENT_ERROR_MESSAGE);
    }

    /**
     * 用户友好的参数异常处理
     *
     * @param response response
     * @param ex       {@link UserArgumentException}
     * @return {@link Result}
     */
    @ResponseBody
    @ExceptionHandler(UserArgumentException.class)
    public Result handle(HttpServletResponse response, UserArgumentException ex) {
        log.warn(ex.getMessage(), ex);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new Result(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 用户友好的异常处理
     *
     * @param response response
     * @param ex       {@link UserFriendlyException}
     * @return {@link Result}
     */
    @ResponseBody
    @ExceptionHandler(UserFriendlyException.class)
    public Result handle(HttpServletResponse response, UserFriendlyException ex) {
        log.warn(ex.getMessage(), ex);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new Result(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * Feign调用异常处理
     *
     * @param response response
     * @param ex       {@link FeignErrorException}
     * @return {@link Result}
     */
    @ResponseBody
    @ExceptionHandler(FeignErrorException.class)
    public Result handle(HttpServletResponse response, FeignErrorException ex) {
        log.error(ex.getMessage(), ex);

        response.setStatus(ex.getHttpCode());
        return new Result(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 其他异常处理
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result handle(HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage(), ex);

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new Result(ErrorCode.B0001.getCode(), DEFAULT_ERROR_MASSAGE);
    }
}
