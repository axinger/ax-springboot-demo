package com.github.axinger;

import com.github.axinger.service.QLExecuteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HelloService unit test class
 */
@SpringBootTest
public class SpringDemoTest {

    @Autowired
    private QLExecuteService qlExecuteService;

    @Test
    public void qlExecuteWithSpringContextTest() {
        Map<String, Object> context = new HashMap<>();
        context.put("name", "Wang");
        String result = (String) qlExecuteService.execute("helloService.hello(name)", context);
        Assert.isTrue(Objects.equals("Hello, Wang!", result), "~~~");
    }
}
