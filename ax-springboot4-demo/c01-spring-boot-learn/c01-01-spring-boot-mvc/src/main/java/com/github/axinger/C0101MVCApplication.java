package com.github.axinger;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class C0101MVCApplication {

    static String My_Env;

    public static void main(String[] args) {
        SpringApplication.run(C0101MVCApplication.class, args);
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
