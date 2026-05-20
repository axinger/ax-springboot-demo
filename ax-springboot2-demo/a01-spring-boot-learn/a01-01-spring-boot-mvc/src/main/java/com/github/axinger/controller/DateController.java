package com.github.axinger.controller;


import com.github.axinger.model.dto.DateVO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@RestController
@RequestMapping("/date")
public class DateController {



    /*
    {
  "date": "2026-05-20 23:45:20",
  "date1": "2026-05-20 23:45:20",
  "localDateTime1": "2026-05-20 23:45:20",
  "localDateTime2": "2026年105-20 23:45:20",
  "localDate1": "2026-05-20"
}
     */
    @PostMapping("/post")
    public Object test1(@RequestBody DateVO model) {
        Map<String, Object> map = new HashMap<>(16);
        return model;
    }

    @GetMapping("/get")
    public Object test2(DateVO model) {
        Map<String, Object> map = new HashMap<>(16);
        return model;
    }

    /// LocalDateFormatterConfig 会提前拦截
    @GetMapping("/get2")
    public Object search(@RequestParam("date") @DateTimeFormat(pattern = "yyyyMMdd HH:mm:ss") LocalDateTime date) {
        // Spring 会自动把 "2026-05-21 10:00:00" 转换为 LocalDateTime 对象
        return date;
    }

    @GetMapping("/get3")
    public Object get3(@RequestParam("date") String date) {
        try {
            // 严格按照你注解里写的格式来解析
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            return "解析成功！转换后的对象是：" + localDateTime;
        } catch (Exception e) {
            // 如果这里报错，说明是 JDK 或格式字符串本身有极隐蔽的问题
            return "手动解析失败：" + e.getMessage();
        }
    }

}
