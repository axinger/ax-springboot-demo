package com.github.axinger;


import com.github.axinger.model.TimePaperPojo;
import com.github.axinger.service.DrawImgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class A0113ApplicationTest {

    @Autowired
    private DrawImgService drawerService;

    @Test
    public void test() {
        TimePaperPojo pojo = new TimePaperPojo();
        String s = drawerService.drawTimePaperImg(pojo);
        System.out.println("s = " + s);
    }
}
