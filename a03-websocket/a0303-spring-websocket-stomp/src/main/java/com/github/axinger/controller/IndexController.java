package com.github.axinger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/index1")
    public String index1() {
        return "index1";
    }

//    @RequestMapping("/index1")
//    public String home() {
//        return "forward:/index1.html";
//    }

    @GetMapping("/index2")
    public String index2() {
        return "index2";
    }

//    @GetMapping("/index2")
//    public ModelAndView index2() {
//        ModelAndView view = new ModelAndView();
//        view.setViewName("index2");
//        return view;
//    }

    @GetMapping("/h5")
    public String h5() {

        return "h5";
    }

    @GetMapping("/chatroom")
    public String chatroom() {

        return "chatroom";
    }


    @GetMapping("/publicExample")
    public String publicExample() {

        return "publicExample";
    }


    @GetMapping("/privateExample")
    public String privateExample() {

        return "privateExample";
    }


}
