package com.bird.service.common.trace.recorder;

import com.bird.service.common.trace.define.FieldTraceDefinition;
import com.bird.service.common.trace.IFieldTraceRecorder;

import java.util.List;

/**
 * @author shaojie
 */
public class NullFieldTraceRecorder implements IFieldTraceRecorder {

    @Override
    public void record(List<FieldTraceDefinition> records) {
        // NoOp
    }
}
