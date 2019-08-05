package com.bird.trace.client.sql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Getter
@RequiredArgsConstructor
public enum TraceSQLType {

    SELECT("select"),

    INSERT("insert"),

    UPDATE("update"),

    DELETE("delete");

    private final String type;
}
