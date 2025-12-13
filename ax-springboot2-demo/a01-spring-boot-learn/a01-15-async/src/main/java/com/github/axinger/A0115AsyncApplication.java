package com.github.axinger;

import com.github.axinger.service.AsyncService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class A0115AsyncApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(A0115AsyncApplication.class, args);
        final AsyncService service = context.getBean(AsyncService.class);

        // 默认线程池
//        service.test1();;
//        service.test2();

        // 内部嵌套
        service.test3();


//        service.test11();
    }


}
