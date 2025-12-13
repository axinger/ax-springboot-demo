package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThymeleafDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThymeleafDemoApplication.class, args);
    }

}

//①， 在public文件夹下的html文件可以通过浏览器中输入文件+后缀名的方式直接访问的。static和templates中不能直接访问，如图示只有名称为public的folder下的文件才可以被直接访问。访问地址为http://localhost:8080/index.html。public文件夹，就相当于在eclipse的web项目中的web-inf文件夹外的文件，是不需要通过服务器内部进行访问的。
//
//②， templates文件夹，是放置模板文件的，因此需要视图解析器来解析它。所以必须通过服务器内部进行访问，也就是要走控制器--服务--视图解析器这个流程才行。并且配置控制器时不能使用@RestController注解来配置，需要使用@Controller。
//
//③， static文件夹，既不能直接访问，也不能通过服务器访问到。因此，这个文件夹，可能是放一些css、图片这样的文件供服务器内部引用。

