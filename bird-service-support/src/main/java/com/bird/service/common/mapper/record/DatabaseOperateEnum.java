package com.bird.service.common.mapper.record;

import com.bird.service.common.mapper.record.handler.DeleteOperateHandler;
import com.bird.service.common.mapper.record.handler.InsertOperateHandler;
import com.bird.service.common.mapper.record.handler.UpdateOperateHandler;
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
    /**  */
    INSERT(InsertOperateHandler.class),
    /**  */
    UPDATE(UpdateOperateHandler.class),
    /**  */
    DELETE(DeleteOperateHandler.class);

    private Class<? extends DatabaseOperateHandler> operateClass;

    public static Map<String,DatabaseOperateHandler> operateHandlers(DatabaseOperateHandler defaultHandler){
        return Arrays.stream(DatabaseOperateEnum.values()).collect(Collectors.toMap(DatabaseOperateEnum::toString,item-> {
            Class<? extends DatabaseOperateHandler> operateClass = item.operateClass;
            try {
                return operateClass.newInstance();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
            return defaultHandler;
        }));
    }
}
