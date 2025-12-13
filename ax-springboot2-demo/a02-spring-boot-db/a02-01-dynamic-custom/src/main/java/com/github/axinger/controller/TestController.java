package com.github.axinger.controller;

import com.github.db1.domain.SysPersonEntity;
import com.github.db1.service.SysPersonService;
import com.github.db2.domain.SysAnimalEntity;
import com.github.db2.service.SysAnimalService;
import com.github.db21.domain.SysDogEntity;
import com.github.db21.service.SysDogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName TestController.java
 * @description TODO
 * @createTime 2022年07月27日 10:56:00
 */
@RestController
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
    public void test1() {
        List<SysPersonEntity> list = personService.list();
        System.out.println("list = " + list);
    }

    @GetMapping("/2")
    public void test2() {
        List<SysDogEntity> list = dogService.list();
        System.out.println("list = " + list);
    }

    @GetMapping("/3")
    public void test3() {
        List<SysAnimalEntity> list = animalService.list();
        System.out.println("list = " + list);
    }

    @GetMapping("/11")
    public void testAB() {
        testService.testAB();
    }

    @GetMapping("/12")
    public void testAC() {
        testService.testAC();
    }


}
