package com.github.axinger;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.github.axinger.sys.domain.SysPersonEntity;
import com.github.axinger.sys.domain.SysUserTotalEntity;
import com.github.axinger.sys.service.SysPersonService;
import com.github.axinger.sys.service.SysUserTotalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MybatisPlusApplicationTests {

    String randomKey = "abcdef1234567890";
    @Autowired
    private SysPersonService sysPersonService;
    @Autowired
    private SysUserTotalService sysUserTotalService;
    @Autowired
    private TestRestTemplate testRestTemplate;

    //    // Jar 启动参数（ idea 设置 Program arguments , 服务器可以设置为启动环境变量 ）
//--mpw.key=d1104d7c3b616f0b
    @Test
    void test1_username() {
        // 生成 16 位随机 AES 密钥
//        String randomKey = AES.generateRandomKey();

        System.out.println("密钥：" + randomKey);

        String data = "root";
        System.out.println("加密前：" + data);

        // 密钥加密
        String result = AES.encrypt(data, randomKey);
        System.out.println("加密后：" + result);

        System.out.println("解密后：" + AES.decrypt(result, randomKey));
    }

    @Test
    void test1_pwd() {
        // 生成16位密钥
        System.out.println("密钥：" + randomKey);

        String data = "123456";
        System.out.println("加密前：" + data);

        // 密钥加密
        String result = AES.encrypt(data, randomKey);
        System.out.println("加密后：" + result);

        System.out.println("解密后：" + AES.decrypt(result, randomKey));
    }

    @Test
    void test2_username() {

        SysUserTotalEntity one = sysUserTotalService.getById(1);

        System.out.println("one = " + one);
    }

    @Test
    void test2_pwd() {

        List<SysUserTotalEntity> list = sysUserTotalService.lambdaQuery().list();
        System.out.println("list = " + list);
    }

    @Test
    void test3() {
        String url = "/person/add";
        String result = testRestTemplate.getForObject(url, String.class);
        System.out.println("result = " + result);
    }

    @Test
    void test4() {
        ParameterizedTypeReference<List<SysPersonEntity>> type = new ParameterizedTypeReference<>() {
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("token", "jim");

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        String url = "/person/list";
        ResponseEntity<List<SysPersonEntity>> exchange = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, type);
        System.out.println("exchange = " + exchange.getBody());
    }

}
