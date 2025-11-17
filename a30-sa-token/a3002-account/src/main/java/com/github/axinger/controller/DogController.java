package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/dog")
public class DogController {

    @GetMapping("/create")
    public Object add() {
        return List.of("/dog/add" + "成功");
    }

    @GetMapping("/read")
    public Object read() {
        return List.of("/dog/read" + "成功");
    }

    @GetMapping("/update")
    public Object update() {
        return List.of("/dog/update" + "成功");
    }

    @GetMapping("/delete")
    public Object delete() {
        return List.of("/dog/delete" + "成功");
    }
}
