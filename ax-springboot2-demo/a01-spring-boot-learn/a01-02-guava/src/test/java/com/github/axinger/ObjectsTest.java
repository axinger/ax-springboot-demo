package com.github.axinger;

import com.google.common.base.MoreObjects;
import org.junit.jupiter.api.Test;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName ObjectsTest.java
 * @description TODO
 * @createTime 2022年05月16日 19:49:00
 */

public class ObjectsTest {

    @Test
    void test_Ordering() {


        UserInfo userInfo = new UserInfo();
        userInfo.setGender(2);
        // Returns "ClassName{x=1}"
        final String x = MoreObjects.toStringHelper(userInfo).add("x", 1).toString();
        System.out.println("x = " + x);


        // Returns "MyObject{x=1}"
//        Objects.toStringHelper("MyObject").add("x", 1).toString();


    }
}
