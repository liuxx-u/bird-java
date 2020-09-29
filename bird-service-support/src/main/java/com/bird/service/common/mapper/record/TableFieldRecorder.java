package com.bird.service.common.mapper.record;

import java.util.List;

/**
 * @author shaojie
 */
public interface TableFieldRecorder {

    /**
     * 记录信息
     * @param record 字段更新记录列表
     */
    void record(List<TableFieldRecord> record);
}

