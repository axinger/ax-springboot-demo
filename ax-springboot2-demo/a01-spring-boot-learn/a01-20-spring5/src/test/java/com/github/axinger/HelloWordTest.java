package com.github.axinger;

import com.github.axinger.helloword.HelloWord;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName HelloWordTest.java
 * @Description TODO
 * @createTime 2022年02月09日 14:39:00
 */

public class HelloWordTest {

    @Test
    public void test1() {
//        ApplicationContext context = new AnnotationConfigApplicationContext(HelloWord.class);

        ApplicationContext context = new ClassPathXmlApplicationContext("hello_word.xml");

        HelloWord helloWord = context.getBean("helloWord", HelloWord.class);
        helloWord.doSome();
    }
}
