package com.github.axinger.controller;


import com.github.axinger.model.dto.UserDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "UserController")
@RequestMapping("/value")
public class ValueController {


    @Value("#{userService.findById('1')}")
//    @Value("@userService.findById('1')")
    private UserDTO userDTO;


    @GetMapping("/1")
    public UserDTO test() {

        return userDTO;
    }
}
