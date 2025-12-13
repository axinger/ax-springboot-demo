package com.github.axinger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.model.Views;
import com.github.axinger.views.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class ViewsTest {

    @SneakyThrows
    @Test
    public void test() {

        ObjectMapper mapper = new ObjectMapper();


        User user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@example.com");
        user.setPasswordHash("123456");

// 使用公共视图
        String publicJson = mapper.writerWithView(Views.Public.class)
                .writeValueAsString(user);
        System.out.println("publicJson = " + publicJson);
// 结果: {"username":"Alice"}

// 使用内部视图
        String internalJson = mapper.writerWithView(Views.Internal.class)
                .writeValueAsString(user);
        System.out.println("internalJson = " + internalJson);
// 结果: {"username":"Alice","email":"alice@example.com"}

    }
}
