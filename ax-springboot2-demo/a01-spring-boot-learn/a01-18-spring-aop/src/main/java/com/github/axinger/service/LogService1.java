package com.github.axinger.service;

import com.github.axinger.annotation.LogListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService1 {

    @LogListener(value = "标记1")
    public String log(String msg) {


        return msg + LocalDateTime.now();
    }
}
