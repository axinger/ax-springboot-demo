package com.github.axinger.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Base64Utils;

import java.util.Objects;

public record Base64RedisSerializer<T>(RedisSerializer<T> redisSerializer) implements RedisSerializer<T> {

    @Override
    public byte[] serialize(T t) throws SerializationException {
        byte[] bytes = this.redisSerializer.serialize(t);
        byte[] result;
        if (ArrayUtils.isEmpty(bytes) || Objects.isNull(bytes)) {
            result = ArrayUtils.EMPTY_BYTE_ARRAY;
        } else {
            result = Base64Utils.encode(bytes);
        }

        return result;
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (ArrayUtils.isEmpty(bytes)) {
            return (T) null;
        } else {
            byte[] unCompressedBytes = Base64Utils.decode(bytes);
            return (T) this.redisSerializer.deserialize(unCompressedBytes);
        }
    }
}
