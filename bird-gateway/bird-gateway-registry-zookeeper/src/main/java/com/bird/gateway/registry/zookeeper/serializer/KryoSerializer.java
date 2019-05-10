package com.bird.gateway.registry.zookeeper.serializer;

import com.bird.gateway.common.enums.SerializeEnum;
import com.bird.gateway.common.exception.SerializerException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public class KryoSerializer implements ZkSerializer {

    @Override
    public byte[] serialize(final Object obj) {
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            //获取kryo对象
            Kryo kryo = new Kryo();
            Output output = new Output(outputStream);
            kryo.writeClassAndObject(output, obj);
            bytes = output.toBytes();
            output.flush();
        } catch (IOException ex) {
            throw new SerializerException("kryo serialize error" + ex.getMessage());
        }
        return bytes;
    }

    @Override
    public Object deserialize(final byte[] bytes) {
        Object object;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            Kryo kryo = new Kryo();
            Input input = new Input(inputStream);
            object = kryo.readClassAndObject(input);
            input.close();
        } catch (IOException e) {
            throw new SerializerException("kryo deSerialize error" + e.getMessage());
        }
        return object;
    }
}
