package com.github.axinger;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URLUtilTests {

    // 标准化URL链接,去除多余的/
    @Test
    public void test1() {
        String url = "http://www.hutool.cn//aaa/bbb";
        // 结果为：http://www.hutool.cn/aaa/bbb
        String normalize = URLUtil.normalize(url);
        System.out.println("normalize = " + normalize);

        System.out.println("normalize2 = " +URLUtil.normalize(url,false,true));

        url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
        // 结果为：http://www.hutool.cn/aaa/bbb?a=1&b=2
        normalize = URLUtil.normalize(url,false,true);

        System.out.println("normalize3 = " + normalize);

//         url = "http://www.hutool.cn/aaa/bbb";
         url = "http://www.hutool.cn//aaa/bbb";
        String path = URLUtil.getPath(url);
        System.out.println("path = " + path);
    }

    @Test
    public void test2() {
        String url = "https://www.baidu.com/s";

        Map<String, Object> params = new HashMap<>();
        params.put("wd", "java");
        params.put("wd", "go");
        String fullUrl = HttpUtil.urlWithForm(url, params, StandardCharsets.UTF_8, true);
        System.out.println("fullUrl = " + fullUrl);

        Map<String, List<String>> params1 = HttpUtil.decodeParams(fullUrl, StandardCharsets.UTF_8);
        System.out.println("params1 = " + params1);
        Map<String, String> params2 = HttpUtil.decodeParamMap(fullUrl, StandardCharsets.UTF_8);
        System.out.println("params2 = " + params2);
    }

    @Test
    public void test3() {
        String body = "366466 - 副本.jpg";
// 结果为：366466%20-%20%E5%89%AF%E6%9C%AC.jpg
        String encode = URLUtil.encode(body);
        System.out.println("encode = " + encode);
    }
}
