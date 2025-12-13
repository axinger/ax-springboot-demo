package com.github.axinger.controller;

import com.github.axinger.sys.domain.SysPersonEntity;
import com.github.axinger.sys.service.SysPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private SysPersonService sysPersonService;

    @GetMapping("/add")
    public Object add() {
        return sysPersonService.save(SysPersonEntity.builder().name("jim").build());
    }

    @GetMapping("/list")
    public List<SysPersonEntity> data() {
        List<SysPersonEntity> list = sysPersonService.list();
        System.out.println("list = " + list);
        return list;
    }


}
