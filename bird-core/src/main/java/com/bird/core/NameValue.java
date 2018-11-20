package com.bird.core;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author liuxx
 * @date 2017/5/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameValue implements Serializable {
    private String label;
    private String value;
}
