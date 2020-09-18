package com.bird.service.common.datarule;

/**
 * 数据规则数据源 策略
 * @author liuxx
 * @date 2018/9/30
 */
public enum RuleSourceStrategy {
    TEXT(0, "用户填写"),
    CHOICE(1,"用户选择"),
    SYSTEM(2, "系统提供");

    private final int key;
    private final String desc;

    private RuleSourceStrategy(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
