package com.bird.service.common.trace;

import com.bird.service.common.trace.handler.DeleteOperateHandler;
import com.bird.service.common.trace.handler.InsertOperateHandler;
import com.bird.service.common.trace.handler.UpdateOperateHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shaojie
 */
@Slf4j
@AllArgsConstructor
public enum DatabaseOperateEnum {
    /**
     * INSERT
     */
    INSERT(InsertOperateHandler.class),
    /**
     * UPDATE
     */
    UPDATE(UpdateOperateHandler.class),
    /**
     * DELETE
     */
    DELETE(DeleteOperateHandler.class);

    private Class<? extends IDatabaseOperateHandler> operateClass;

    public static Map<String, IDatabaseOperateHandler> operateHandlers(IDatabaseOperateHandler defaultHandler) {
        return Arrays.stream(DatabaseOperateEnum.values()).collect(Collectors.toMap(DatabaseOperateEnum::toString, item -> {
            Class<? extends IDatabaseOperateHandler> operateClass = item.operateClass;
            try {
                return operateClass.newInstance();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return defaultHandler;
        }));
    }
}
