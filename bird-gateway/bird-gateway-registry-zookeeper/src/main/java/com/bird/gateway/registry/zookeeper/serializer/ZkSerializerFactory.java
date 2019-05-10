package com.bird.gateway.registry.zookeeper.serializer;

import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public class ZkSerializerFactory {

    private static final ServiceLoader<ZkSerializer> SERVICE_LOADER = ServiceLoader.load(ZkSerializer.class);

    /**
     * product  ZkSerializer with className.
     *
     * @param className className
     * @return ZkSerializer zk serializer
     */
    public static ZkSerializer of(final String className) {
        return StreamSupport.stream(SERVICE_LOADER.spliterator(), false)
                .filter(service -> Objects.equals(service.getClass().getName().substring(service.getClass().getName().lastIndexOf(".") + 1), className))
                .findFirst()
                .orElse(new KryoSerializer());
    }
}
