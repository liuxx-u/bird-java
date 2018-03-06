package com.bird.service.common.service.query;

/**
 * Created by liuxx on 2017/6/22.
 */
public interface FilterOperate {
    String AND = "and";
    String OR = "or";

    String EQUAL = "equal";
    String NOTEQUAL = "notequal";

    String LESS = "less";
    String LESSOREQUAL = "lessorequal";

    String GREATER = "greater";
    String GREATEROREQUAL = "greaterorequal";

    String STARTWITH = "startswith";
    String ENDWITH = "endwith";
    String CONTAINS = "contains";
}
