package com.bird.service.common.incrementer;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * @author liuxx
 * @since 2020/5/20
 */
public class StringKeyGenerator implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        return null;
    }

    @Override
    public String nextUUID(Object entity) {
        return UUIDHexGenerator.generate();
    }
}
