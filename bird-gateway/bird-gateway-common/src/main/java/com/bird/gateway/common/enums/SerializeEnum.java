package com.bird.gateway.common.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public enum SerializeEnum {

    /**
     * Jdk serialize protocol enum.
     */
    JDK("jdk"),

    /**
     * Kryo serialize protocol enum.
     */
    KRYO("kryo"),

    /**
     * Hessian serialize protocol enum.
     */
    HESSIAN("hessian"),

    /**
     * Protostuff serialize protocol enum.
     */
    PROTOSTUFF("protostuff");

    private final String serialize;

    SerializeEnum(final String serialize) {
        this.serialize = serialize;
    }

    /**
     * get serialize.
     *
     * @return serialize
     */
    public String getSerialize() {
        return serialize;
    }


    /**
     * Acquire serialize protocol serialize protocol enum.
     *
     * @param serialize the serialize protocol
     * @return the serialize protocol enum
     */
    public static SerializeEnum acquire(final String serialize) {
        Optional<SerializeEnum> serializeEnum =
                Arrays.stream(SerializeEnum.values())
                        .filter(v -> Objects.equals(v.getSerialize(), serialize))
                        .findFirst();
        return serializeEnum.orElse(SerializeEnum.KRYO);

    }
}
