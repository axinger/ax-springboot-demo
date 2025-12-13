package com.github.axinger;

import cn.hutool.cron.CronUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/start")
    public void start() {
//        考虑到Quartz表达式的兼容性，且存在对于秒级别精度匹配的需求，Hutool可以通过设置使用秒匹配模式来兼容。
        CronUtil.setMatchSecond(true);
        CronUtil.start();

    }

    @GetMapping("/stop")
    public void stop() {
        CronUtil.stop();
    }
}
