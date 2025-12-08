package com.github.axinger.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PersonSerializer extends JsonSerializer<JacksonUser> {
    @Override
    public void serialize(JacksonUser person, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("email2", "default@example.com"); // 新增字段！
        gen.writeEndObject();
    }
}
