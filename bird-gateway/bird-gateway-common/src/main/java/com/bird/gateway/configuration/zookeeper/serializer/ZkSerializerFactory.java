package com.bird.gateway.configuration.zookeeper.serializer;

import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * @author liuxx
 * @date 2018/11/27
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
                .filter(service ->
                        Objects.equals(service.getClass().getName().substring(service.getClass().getName().lastIndexOf(".") + 1,
                                service.getClass().getName().length()),
                                className)).findFirst().orElse(new KryoSerializer());
    }
}
