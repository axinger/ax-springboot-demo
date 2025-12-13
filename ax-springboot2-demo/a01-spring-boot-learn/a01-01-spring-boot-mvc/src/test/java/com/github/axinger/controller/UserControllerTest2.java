package com.github.axinger.controller;

import com.github.axinger.model.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class UserControllerTest2 {


    @Autowired
    private TestRestTemplate restTemplate;

//    @Autowired
//    private RestTemplate restTemplate;

    @Test
    public void testGetUserById() {

        UserDTO user = restTemplate.getForObject("/user/getUserById/{id}", UserDTO.class, 1);
        Assert.notNull(user, "查询结果为null");
        System.out.println(user);
//        assertEquals("jim2", user.getName());
        assertEquals("jim", user.getName());

    }


}
