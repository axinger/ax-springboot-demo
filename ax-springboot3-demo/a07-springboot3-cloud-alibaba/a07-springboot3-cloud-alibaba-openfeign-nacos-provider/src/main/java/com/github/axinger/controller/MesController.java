package com.github.axinger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MesController.java
 * @description TODO
 * @createTime 2022年06月24日 14:59:00
 */
@RestController
@RequestMapping("/mes")
@Slf4j
public class MesController {

    @GetMapping("/test1")
    public Map add() {
        Map map = new HashMap();
        map.put("name", "jim");
        map.put("date", LocalDateTime.now());
        return map;
    }
}
