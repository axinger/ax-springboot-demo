package com.github.axinger.jsonSchema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Set;

@Service
public class JsonValidationService {

    private final JsonSchema userSchema;

    public JsonValidationService() throws Exception {
        // 1. 加载Schema文件
        InputStream schemaStream = new ClassPathResource("schemas/user-schema.json").getInputStream();
        JsonNode schema = new ObjectMapper().readTree(schemaStream);

        // 2. 创建Schema工厂并加载Schema
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        this.userSchema = factory.getSchema(schema);
    }

    /**
     * 验证JSON字符串是否符合用户Schema
     *
     * @param jsonString 待验证的JSON字符串
     * @return 验证错误信息集合，为空则表示验证通过
     */
    public Set<ValidationMessage> validateUserJson(String jsonString) {
        try {
            // 1. 将JSON字符串解析为JsonNode
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonString);

            // 2. 执行验证
            return userSchema.validate(jsonNode);

        } catch (Exception e) {
            throw new RuntimeException("JSON解析失败", e);
        }
    }
}
