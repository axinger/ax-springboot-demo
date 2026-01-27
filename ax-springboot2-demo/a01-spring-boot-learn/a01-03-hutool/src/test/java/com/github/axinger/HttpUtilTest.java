package com.github.axinger;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HttpUtilTest {

    @SneakyThrows
    @Test
    public void test() {
        String str = "http://localhost:10707/order/test4?orderId=123%2B456";
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        //根据RFC3986规范，URL参数值中+是安全字符，无需转义
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", "123+456");
//        paramMap.put("orderId2", URLEncoder.encode("1+2", StandardCharsets.UTF_8));


        String url2 = HttpUtil.urlWithFormUrlEncoded("http://localhost:10707/#/order/test4", paramMap, StandardCharsets.UTF_8);
        System.out.println("url2 = " + url2);
        String url3 = HttpUtil.urlWithForm("http://localhost:10707/#/order/test4", paramMap, StandardCharsets.UTF_8, true);
        System.out.println("url3 = " + url3);

        String url4 = HttpUtil.urlWithForm("http://localhost:10707/#/order/test4", paramMap, StandardCharsets.UTF_8, false);
        System.out.println("url4 = " + url4);

        String encodedQuery = URLUtil.encodeQuery(url4, StandardCharsets.UTF_8);
        System.out.println("encodedQuery = " + encodedQuery);

        String decodedQuery = URLUtil.decode(url4, StandardCharsets.UTF_8, true);
        System.out.println("decodedQuery = " + decodedQuery);

        String params = HttpUtil.toParams(paramMap);
        System.out.println("params = " + params);



        String params1 = HttpUtil.toParams(paramMap);
        /// 所有的都编码
        String encode2 = URLUtil.encodeAll(params1, StandardCharsets.UTF_8);
        System.out.println("encode2 = " + encode2);

        String fullUrl2 = HttpUtil.urlWithForm("http://localhost:10707/order/test4", encode2, StandardCharsets.UTF_8, false);
        System.out.println("fullUrl2 = " + fullUrl2);


        String encode3 = URLUtil.encodeAll(HttpUtil.toParams(paramMap), StandardCharsets.UTF_8);
        System.out.println("encode3 = " + encode3);
    }
}
