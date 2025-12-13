package com.github.axinger.JTest;

import com.github.axinger.xml.model.Dog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


//@RunWith(SpringJUnit4ClassRunner.class)

//@ExtendWith(SpringExtension.class)// junit5
//@ContextConfiguration("classpath:bean2.xml")
@SpringJUnitConfig(locations = "classpath:bean2.xml") // 复合
public class JTest4 {
    @Autowired
    private Dog dog;

    @Test
    public void test() {

        System.out.println("dog = " + dog);
    }
}
