package com.cczcrv.core.mapper;

/**
 * Created by liuxx on 2017/6/27.
 */
public abstract class AbstractMybatisProvider {

    protected String ExpandIdAndToString(long[] ids) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = ids.length; i < len; i++) {
            if (i == len - 1) {
                sb.append(ids[i]);
            } else {
                sb.append(ids[i]);
                sb.append(",");
            }
        }
        return sb.toString();
    }

    protected String ExpandKeysAndToString(String[] keys) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = keys.length; i < len; i++) {
            String key = "'" + keys[i] + "'";
            if (i == len - 1) {
                sb.append(key);
            } else {
                sb.append(key);
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
