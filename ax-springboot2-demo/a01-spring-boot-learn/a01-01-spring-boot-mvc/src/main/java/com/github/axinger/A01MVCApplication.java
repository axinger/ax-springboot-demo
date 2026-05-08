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


        // 1. 查看操作系统名称
        String osName = System.getProperty("os.name");
        System.out.println("OS Name: " + osName);

        // 2. 查看文件分隔符（Linux 是 /，Windows 是 \）
        String fileSeparator = System.getProperty("file.separator");
        System.out.println("File Separator: " + fileSeparator);

        // 3. 查看路径分隔符（Linux 是 :，Windows 是 ;）
        String pathSeparator = System.getProperty("path.separator");
        System.out.println("Path Separator: " + pathSeparator);

        // 4. 查看工作目录
        String userDir = System.getProperty("user.dir");
        System.out.println("User Dir: " + userDir);

        // 5. 核心判断
        boolean isLinux = osName.toLowerCase().contains("linux");
        boolean isWSL = userDir.contains("/mnt/") ||
                System.getenv("WSL_DISTRO_NAME") != null;

        System.out.println("\n=== 结论 ===");
        if (isWSL) {
            System.out.println("✅ 运行在 WSL Linux 环境");
        } else if (isLinux) {
            System.out.println("✅ 运行在原生 Linux 环境");
        } else {
            System.out.println("❌ 运行在 Windows 环境");
        }
    }

    @Value("${spring.profiles.active}")
    public void setMyEnv(String myEnv) {
        My_Env = myEnv;
    }


}
