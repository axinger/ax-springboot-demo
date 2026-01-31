package ax.com.dubbo.provider.controller;

import com.ax.api.service.TestDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName TestController.java
 * @Description TODO
 * @createTime 2021年12月18日 22:02:00
 */
@RestController
public class TestController {


    @DubboReference(version = "1.0.0"
//            check = false,
            , interfaceClass = TestDubboService.class)
    TestDubboService testDubboService;


    @GetMapping("/test")
    public Object account() {
        System.out.println("进入了ConsumerController.............");
        final Object o = testDubboService.test1("生产者自己调用");
        System.out.println("Dubbo调用返回值 = " + o);
        return o;
    }

}
