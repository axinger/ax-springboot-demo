package com.github.axinger.controller;

import com.github.axinger.domain.School;
import com.github.axinger.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school")
public class SchoolController {


    @Autowired
    private SchoolService service;


    @GetMapping("/list")
    public List<School> list() {
        return service.list();
    }

    @GetMapping("/one")
    public School one(Long id) {
        return service.getById(id);
    }
}
