package com.github.axinger.util;


import cn.hutool.core.io.IoUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class CompressUtil {

    public static byte[] serialize(String graph) throws SerializationException {
        if (graph == null) {
            return new byte[0];
        }
        try {
            byte[] bytes = graph.getBytes();
            @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
            @Cleanup GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(bytes);
            gzip.finish();
            byte[] result = bos.toByteArray();
            return Base64Utils.encode(result);
        } catch (Exception e) {
            throw new SerializationException("Gzip Serialization Error", e);
        }
    }

    public static String deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            // First Base64 decode
            byte[] compressedBytes = Base64Utils.decode(bytes);
            @Cleanup ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedBytes);
            @Cleanup GZIPInputStream gzip = new GZIPInputStream(inputStream);
            @Cleanup ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            IoUtil.copy(gzip, outputStream);
            return outputStream.toString();
        } catch (Exception e) {
            throw new SerializationException("Gzip deserialize error", e);
        }
    }
}
