package com.bird.core;

/**
 * @author liuxx
 * @date 2018/5/3
 */
public enum Position {
    /**
     * 左上
     */
    LEFT_TOP(1),
    /**
     * 左中
     */
    LEFT_MIDDLE(2),
    /**
     * 左下
     */
    LEFT_BOTTOM(3),
    /**
     * 中上
     */
    MIDDLE_TOP(4),
    /**
     * 中中
     */
    MIDDLE_MIDDLE(5),
    /**
     * 中下
     */
    MIDDLE_BOTTOM(6),
    /**
     * 右上
     */
    RIGHT_TOP(7),
    /**
     * 右中
     */
    RIGHT_MIDDLE(8),
    /**
     * 右下
     */
    RIGHT_BOTTOM(9);


    Position(Integer value) {
        this.value = value;
    }

    private final Integer value;

    @Override
    public String toString() {
        return this.value.toString();
    }
}
