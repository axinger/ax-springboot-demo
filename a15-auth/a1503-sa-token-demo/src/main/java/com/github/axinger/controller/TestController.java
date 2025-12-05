package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/")
@RequiredArgsConstructor

public class TestController {


    @GetMapping("/test1")
    public Result<?> test1() {
        return Result.success("test1");
    }

    @GetMapping("/test2")
    public Result<?> test2() {
        return Result.success("test2");
    }


    @GetMapping("/test3")
    public Result<?> test3() {
        return Result.success("test3");
    }


    @GetMapping("/test4")
    public Result<?> test4() {
        return Result.success("test4");
    }


}
