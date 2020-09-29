package com.bird.service.common.mapper.record.recorder;


import com.bird.service.common.mapper.record.TableFieldRecord;
import com.bird.service.common.mapper.record.TableFieldRecorder;

import java.util.List;

/**
 * @author shaojie
 */
public class NoOpTableFieldRecorder implements TableFieldRecorder {

    @Override
    public void record(List<TableFieldRecord> records) {
        // NoOp
    }
}
