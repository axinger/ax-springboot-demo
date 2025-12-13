package com.github.axinger;

import com.github.axinger.controller.TestService1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class A0202SyncTransactionApplicationTest {


    @Autowired
    private TestService1 testService1;


    @Test
    void test4() {
        testService1.test4();
    }


}


