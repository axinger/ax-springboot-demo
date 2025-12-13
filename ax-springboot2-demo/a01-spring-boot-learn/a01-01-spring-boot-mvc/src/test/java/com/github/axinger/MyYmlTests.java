package com.github.axinger;

import com.github.axinger.model.bean.MyYmlBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Map;
import java.util.Properties;

@SpringBootTest
public class MyYmlTests {

    @Autowired
    private MyYmlBean myYmlBean;

    @Test
    void test1() {
        System.out.println("userConfig.getList() = " + myYmlBean);
        String username = myYmlBean.getUser().getUsername();
        System.out.println("username = " + username);

    }

    @Test
    public void test2() {
        // 1.获取 yml 文件资源
        Resource resource = new ClassPathResource("axinger.yml");
        // 2.解析 yml 文件
        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(resource);
        // 3.读取解析结果
        Properties prop = bean.getObject();
        for (Object key : prop.keySet()) {
            System.out.println(key + " = " + prop.get(key));
        }
    }

    @Test
    public void test3() {
        // 1.获取 yml 文件资源
        Resource resource = new ClassPathResource("axinger.yml");
        // 2.解析 yml 文件
        YamlMapFactoryBean bean = new YamlMapFactoryBean();
        bean.setResources(resource);
        // 3.读取解析结果
        Map<String, Object> map = bean.getObject();

        System.out.println("map = " + map);
    }
}
