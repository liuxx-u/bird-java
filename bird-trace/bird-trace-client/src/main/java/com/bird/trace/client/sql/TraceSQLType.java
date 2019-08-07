package com.bird.trace.client.sql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Getter
@RequiredArgsConstructor
public enum TraceSQLType {

    NONE("none"),

    SELECT("select"),

    INSERT("insert"),

    UPDATE("update"),

    DELETE("delete");

    private final String type;

    /**
     * 根据SQL获取SQLType
     * @param sql sql
     * @return TraceSQLType
     */
    public static TraceSQLType acquire(final String sql) {
        Optional<TraceSQLType> serializeEnum =
                Arrays.stream(TraceSQLType.values())
                        .filter(v -> StringUtils.startsWithIgnoreCase(sql, v.type))
                        .findFirst();

        return serializeEnum.orElse(TraceSQLType.NONE);
    }
}
