package com.github.axinger;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BaseEncryptTest {
    //其实直接@Autowired也可以
    //但是如果有其他实现了StringEncryptor接口的Bean请使用@Resource指定
    @Resource
    StringEncryptor stringEncryptor;


    @Test
    void encrypt() {
        System.out.println("key=" + stringEncryptor.encrypt("Abcd#1234"));
    }
}
