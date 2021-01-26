package com.bird.service.common.grid.exception;

import com.bird.core.exception.ErrorCode;

/**
 * @author liuxx
 * @since 2021/1/22
 */
public class GridException extends RuntimeException {


    public GridException(){}

    public GridException(String message) {
        super(message);
    }
}
