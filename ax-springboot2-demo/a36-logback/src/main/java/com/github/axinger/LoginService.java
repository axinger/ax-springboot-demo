package com.github.axinger;

import lombok.CustomLog;
import org.springframework.stereotype.Service;

@Service
//@Slf4j(topic = "11")
//@CustomLog(topic = "MyLog")
@CustomLog
//@Log
public class LoginService {

    public void login() {
        log.info("普通日志===============");
        log.logA.info("登录日志=======================A类");
        log.logB.info("注册日志=======================B类");
    }
}
