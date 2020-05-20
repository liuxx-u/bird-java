package com.bird.core;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 键值
 *
 * @author liuxx
 * @date 2017/5/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameValue implements Serializable {

    /**
     * 显示值
     */
    private String label;
    /**
     * 存储值
     */
    private String value;
}
