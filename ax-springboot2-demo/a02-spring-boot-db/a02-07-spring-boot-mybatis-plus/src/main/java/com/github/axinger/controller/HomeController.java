package com.github.axinger.controller;

import com.github.axinger.sys.domain.SysPersonEntity;
import com.github.axinger.sys.service.SysPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private SysPersonService sysPersonService;

    @GetMapping("/data")
    public Object data() {
        return List.of(1);
    }

    @GetMapping("/list")
    public Object test() {
        List<SysPersonEntity> list = sysPersonService.list();
        return list;
    }

}
