package com.bird.service.common.trace.recorder;

import com.bird.service.common.trace.define.ColumnTraceDefinition;
import com.bird.service.common.trace.IColumnTraceRecorder;

import java.util.List;

/**
 * @author shaojie
 */
public class NoOpColumnTraceRecorder implements IColumnTraceRecorder {

    @Override
    public void record(List<ColumnTraceDefinition> records) {
        // NoOp
    }
}
