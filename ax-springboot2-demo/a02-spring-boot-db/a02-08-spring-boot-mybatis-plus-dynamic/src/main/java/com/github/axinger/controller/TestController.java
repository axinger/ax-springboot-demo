package com.github.axinger.controller;

import com.github.axinger.db1.service.SysPersonService;
import com.github.axinger.db2.service.SysAnimalService;
import com.github.axinger.db2.service.SysDogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "TestController", description = "测试控制器")
public class TestController {


    @Autowired
    private SysPersonService personService;

    @Autowired
    private SysDogService dogService;

    @Autowired
    private SysAnimalService animalService;

    @Autowired
    private TestService testService;

    @GetMapping("/1")
    public Object test1() {
        return personService.list();
    }

    @GetMapping("/2")
    public Object test2() {
        return dogService.list();
    }

    @GetMapping("/3")
    public void test3(@RequestParam(required = true) Boolean error) {
        testService.testAB(error);
    }


    @GetMapping("/4")
    public void testAC(@RequestParam(required = true) Boolean error) {
        testService.testAC(error);
    }


}
