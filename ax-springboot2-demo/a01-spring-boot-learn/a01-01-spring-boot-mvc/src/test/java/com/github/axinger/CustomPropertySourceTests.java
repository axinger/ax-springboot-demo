package com.github.axinger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomPropertySourceTests {

    @Autowired
    ConfigurableEnvironment environment;
    @Value("${app.currentTime}")
    private Long currentTime;
    @Value("${app.randomValue}")
    private String randomValue;
    @Value("${app.currentTime2}")
    private Long currentTime2;
    @Value("${app.randomValue2}")
    private String randomValue2;

    @Test
    public void test1() {
        System.out.println("currentTime = " + currentTime);
        System.out.println("randomValue = " + randomValue);

        System.out.println("currentTime2 = " + currentTime2);
        System.out.println("randomValue2 = " + randomValue2);

        // java定义返回值,yaml引用
        // 可以通过名称获取特定的属性源
        PropertySource<?> source = environment.getPropertySources().get("customProperties");
        System.out.println("source = " + source);

// 可以基于名称调整属性源顺序
//        environment.getPropertySources().remove("customProperties");
//        environment.getPropertySources().addAfter("anotherSource", new MapPropertySource(...));
    }
}
