package com.github.axinger;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.retry.annotation.EnableRetry;

@ComponentScan(excludeFilters = {
        // 去除装配
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        com.github.axinger.b.UserService.class
                })
})
@SpringBootApplication
//@EnableRetry(proxyTargetClass = true)
@EnableRetry
@Slf4j

/*
 @ServletComponentScan作用 SpringBootApplication 上使用@ServletComponentScan 注解后
 Servlet可以直接通过@WebServlet注解自动注册
 Filter可以直接通过@WebFilter注解自动注册
 Listener可以直接通过@WebListener 注解自动注册
 */
@ServletComponentScan


//@ServletComponentScan(basePackages = "com.github.axinger.filter")
public class A01MVCApplication {

    static String My_Env;

    public static void main(String[] args) {
        SpringApplication.run(A01MVCApplication.class, args);
        log.info("环境变量={}", My_Env);
        log.error("环境变量={}", My_Env);
        log.debug("环境变量={}", My_Env);
        log.warn("环境变量={}", My_Env);
        log.trace("环境变量={}", My_Env);
    }

    @Value("${spring.profiles.active}")
    public void setMyEnv(String myEnv) {
        My_Env = myEnv;
    }


}
