package com.bird.web.file.upload.validator;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 验证结果
 *
 * @author liuxx
 * @date 2018/4/28
 */
@Getter
@Setter
public class ValidateResult implements Serializable {
    private boolean success;
    private String errorInfo;

    public static ValidateResult success(){
        return new ValidateResult(true,null);
    }

    public static ValidateResult fail(String errorInfo){
        return new ValidateResult(false,errorInfo);
    }

    public ValidateResult(){}

    public ValidateResult(boolean success,String errorInfo){
        this.success = success;
        this.errorInfo = errorInfo;
    }
}
