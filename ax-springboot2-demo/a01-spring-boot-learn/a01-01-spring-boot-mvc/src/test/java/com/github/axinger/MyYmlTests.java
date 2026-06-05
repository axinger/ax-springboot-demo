package com.github.axinger;

import com.github.axinger.model.properties.DemoPropertySourceProperties;
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
    private DemoPropertySourceProperties demoPropertySourceProperties;

    @Test
    void test1() {
        System.out.println("demoPropertySourceProperties = " + demoPropertySourceProperties);
        String username = demoPropertySourceProperties.getUser().getUsername();
        System.out.println("username = " + username);
    }

    @Test
    public void test2() {
        Resource resource = new ClassPathResource("demo-source.yml");
        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(resource);
        Properties prop = bean.getObject();
        for (Object key : prop.keySet()) {
            System.out.println(key + " = " + prop.get(key));
        }
    }

    @Test
    public void test3() {
        Resource resource = new ClassPathResource("demo-source.yml");
        YamlMapFactoryBean bean = new YamlMapFactoryBean();
        bean.setResources(resource);
        Map<String, Object> map = bean.getObject();
        System.out.println("map = " + map);
    }
}
