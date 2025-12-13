package com.github.axinger;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

/// DigestUtils 是 Spring Framework 提供的一个实用工具类
public class DigestUtilsTests {

    @Test
    public void testMD5() {
        String password = "123456";
        String md5DigestPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println("md5加密密码: " + md5DigestPassword);
//        输出结果：
//        256加密密码: e10adc3949ba59abbe56e057f20f883e
    }

    @Test
    public void testSHA1() {
//        String password = "123456";
//        String sha1DigestPassword = DigestUtils.sha1DigestAsHex(password.getBytes());
//        System.out.println("sha1加密密码: " + sha1DigestPassword);
//        输出结果：
//        sha1加密密码: 7c4a8d09ca3762af61e59520943dc26494f8941b
    }


    @Test
    public void testSHA256() {

//        String password = "123456";
//        String sha256DigestPassword = DigestUtils.sha256DigestAsHex(password.getBytes());
//        System.out.println("sha256加密密码: " + sha256DigestPassword);
//        输出结果：
//        256加密密码: e10adc3949ba59abbe56e057f20f883e


//        if (SystemUtils.IS_JAVA_11) {
//            System.out.println("当前Java版本是11");
//        } else if (SystemUtils.IS_JAVA_17) {
//            System.out.println("当前Java版本是17");
//        } else {
//            System.out.println("当前Java版本不是11或17");
//        }
//        输出：当前Java版本不是11或17


    }

}
