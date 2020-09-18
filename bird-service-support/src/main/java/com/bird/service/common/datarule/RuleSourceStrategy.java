package com.bird.service.common.datarule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据规则数据源 策略
 * @author liuxx
 * @date 2018/9/30
 */
@Getter
@RequiredArgsConstructor
public enum RuleSourceStrategy {
    /**
     * 用户填写
     */
    TEXT("TEXT", "用户填写"),
    /**
     * 用户选择
     */
    CHOICE("CHOICE", "用户选择"),
    /**
     * 系统提供
     */
    SYSTEM("SYSTEM", "系统提供");

    private final String key;
    
    private final String desc;
}
