package com.github.axinger.controller;

import com.github.axinger.sys.domain.EmployeeEntity;
import com.github.axinger.sys.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/emp")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/add")
    public Object add() {
        return employeeService.save(EmployeeEntity.builder().name("jim").build());
    }

    @GetMapping("/list")
    public Object data() {
//        MyTenantLineHandler.setTenantId("100");
        List<EmployeeEntity> list = employeeService.list();
        System.out.println("list = " + list);
        return list;
    }


}
