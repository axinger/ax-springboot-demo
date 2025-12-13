package com.github.axinger.controller;

import com.github.axinger.domain.Student;
import com.github.axinger.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
// @CacheConfig(cacheNames = "st")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public List<Student> list() {
        return studentService.list();
    }

    @GetMapping("/one")
    public Student one(Long id) {
        return studentService.getById(id);
    }

    @GetMapping("/add")
    @CachePut(value = "lastName", key = "#student.name")
    public Student add(Student student) {
        studentService.save(student);
        return student;
    }

    /**
     * unless = "#result == null" 的意思就是，当返回值为null时，就不缓存
     *
     * @param name
     * @return
     */
    @GetMapping("/name")
    @Cacheable(value = "lastName", key = "#name", unless = "#result == null")
    public Student name(String name) {
        Student one = studentService.lambdaQuery()
                .eq(Student::getName, name)
                .orderByDesc(Student::getId)
                .last("limit 1")
                .one();
        System.out.println("one = " + one);
        return one;
    }

}

