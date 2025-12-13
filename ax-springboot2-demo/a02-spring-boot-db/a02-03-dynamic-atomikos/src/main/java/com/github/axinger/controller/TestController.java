package com.github.axinger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    TestService testService;


    @GetMapping("/3")
    public void test3(@RequestParam(required = true) Boolean error) {
        testService.testAB(error);
    }


    @GetMapping("/4")
    public void testAC(@RequestParam(required = false, defaultValue = "false") Boolean error) {
        testService.testAC(error);
    }

    @GetMapping("/5")
    public void test5(@RequestParam(required = false, defaultValue = "false") Boolean error) {
        testService.asyncMethod(error);
    }

    @GetMapping("/6")
    public void test6() {
        testService.testTranslation();
    }


}
