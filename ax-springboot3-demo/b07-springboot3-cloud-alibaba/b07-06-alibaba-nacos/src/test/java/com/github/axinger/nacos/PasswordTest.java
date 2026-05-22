package com.github.axinger.nacos;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {

    @Test
    void contextLoads() {
        String nacos = new BCryptPasswordEncoder().encode("nacos");
        System.out.println("nacos = " + nacos);
    }
}
