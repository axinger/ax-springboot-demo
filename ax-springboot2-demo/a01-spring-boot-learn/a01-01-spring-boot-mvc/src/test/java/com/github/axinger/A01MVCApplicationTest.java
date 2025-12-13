package com.github.axinger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "axinger.user.username=张三,李四"
})
public class A01MVCApplicationTest {


    @Value("#{'${axinger.user.username:}'.split(',')}")
    private List<String> usernameList;

    @Value("${axinger.user.username}")
    private List<String> usernameList2;

    @Autowired
    private Environment environment;

    @Test
    public void test1() {

        System.out.println("usernameList = " + usernameList);
        System.out.println("usernameList2 = " + usernameList2);

        String property = environment.getProperty("axinger.user.username");
        System.out.println("property = " + property);
        String requiredProperty = environment.getRequiredProperty("axinger.user.username");
        System.out.println("requiredProperty = " + requiredProperty);
    }


}
