package com.github.axinger;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.axing.common.json.util.JsonUtil;
import com.github.axinger.model.bean.AxingerPersonProperties;
import com.github.axinger.model.bean.AxingerUserProperties;
import com.github.axinger.model.bean.MyUserProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class PropertiesTests {

    @Autowired
    private AxingerUserProperties axingerUserProperties;
    @Autowired
    private AxingerPersonProperties axingerPersonProperties;
    @Resource
    private MyUserProperties myUserProperties;


    @Test
    void test1() {
//        System.out.println("axingerPersonProperties = " + axingerPersonProperties);
//        System.out.println("axingerPersonProperties = " + JSONUtil.toJsonPrettyStr(axingerPersonProperties));
        System.out.println("axingerPersonProperties = " + JsonUtil.toPrettyPrinterJson(axingerPersonProperties));
    }


    @Test
    void test2() {

        String text1 = axingerPersonProperties.getText1();
        String text2 = axingerPersonProperties.getText2();
        String text3 = axingerPersonProperties.getText3();
        System.out.println("text1 = " + text1);
        System.out.println("text1.length() = " + text1.length());
        List<String> list1 = StrUtil.split(text1, ",");
        System.out.println("list1 = " + list1);
        System.out.println("list1.size() = " + list1.size());

        System.out.println("text2 = " + text2);
        System.out.println("text2.length() = " + text2.length());
        List<String> list2 = StrUtil.split(text2, ",");
        System.out.println("list2 = " + list2);
        System.out.println("list2.size() = " + list2.size());


        System.out.println("text3 = " + text3);
        System.out.println("text3.length() = " + text3.length());
        List<String> list3 = StrUtil.split(text3, ",");
        System.out.println("list3 = " + list3);
        System.out.println("list3.size() = " + list3.size());

    }

    @Test
    void test3() {
        System.out.println("axingerUserProperties = " + axingerUserProperties);
        System.out.println("axingerPersonProperties = " + axingerPersonProperties);
        System.out.println("myUserProperties = " + myUserProperties);
    }
}
