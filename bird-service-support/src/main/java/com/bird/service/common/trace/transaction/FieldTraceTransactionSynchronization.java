package com.bird.service.common.trace.transaction;

import com.bird.service.common.trace.FieldTraceAppender;
import com.bird.service.common.trace.define.FieldTraceDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @since 2020/10/13
 */
public class FieldTraceTransactionSynchronization implements TransactionSynchronization {

    private static final String RESOURCE_KEY = "FIELD_TRACE_RESOURCE";
    private static final FieldTraceTransactionSynchronization INSTANCE = new FieldTraceTransactionSynchronization();

    private FieldTraceTransactionSynchronization() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterCommit() {
        Object resource = TransactionSynchronizationManager.getResource(RESOURCE_KEY);
        if (resource instanceof List) {
            List<FieldTraceDefinition> list = (List<FieldTraceDefinition>) resource;
            // 将列表中的记录换入到交换器中
            list.forEach(FieldTraceAppender::append);
        }
    }

    @SuppressWarnings("unchecked")
    public static void appendResource(FieldTraceDefinition traceDefinition) {
        // 从事务管理器中获取资源信息
        Object resource = TransactionSynchronizationManager.getResource(RESOURCE_KEY);
        if (resource == null) {
            // 如果获取不到, 说明当前事务是新创建的事务, 需要绑定资源, 注册同步器
            // 1.绑定资源
            resource = new ArrayList<FieldTraceDefinition>();
            TransactionSynchronizationManager.bindResource(RESOURCE_KEY, resource);
            // 2.注册同步器
            TransactionSynchronizationManager.registerSynchronization(INSTANCE);
        }
        List<FieldTraceDefinition> definitions = (List<FieldTraceDefinition>) resource;
        // 添加一个
        definitions.add(traceDefinition);
    }
}
