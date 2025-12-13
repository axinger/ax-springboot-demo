package com.github.axinger.jsonSchema;

import com.networknt.schema.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class JsonValidationController {

    @Autowired
    private JsonValidationService validationService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody String requestBody) {

        // 使用JSON Schema进行验证
        Set<ValidationMessage> errors = validationService.validateUserJson(requestBody);

        if (!errors.isEmpty()) {
            // 验证失败，返回错误详情
            StringBuilder errorMsg = new StringBuilder("JSON验证失败: ");
            for (ValidationMessage error : errors) {
                errorMsg.append(error.getMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errorMsg.toString());
        }

        // 验证通过，继续处理业务逻辑...
        // ... 例如，将requestBody反序列化为User对象

        return ResponseEntity.ok("用户创建成功");
    }
}
