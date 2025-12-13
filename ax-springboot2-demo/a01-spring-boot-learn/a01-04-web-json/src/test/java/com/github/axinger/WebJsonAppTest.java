package com.github.axinger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.entity.Pig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebJsonAppTest {
    @Autowired
    private Pig pig;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void pig() {

        System.out.println("new Pig() = " + new Pig());
        System.out.println("pig = " + pig);

        System.out.println("objectMapper = " + objectMapper);
    }
}
