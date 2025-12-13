package com.github.axinger;

import com.github.axinger.domain.Student;
import com.github.axinger.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTest {

    @Autowired
    StudentService studentService;

    @Test
    void test1() {
        String name = "jim22";

        Student one = studentService.lambdaQuery()
                .eq(Student::getName, name)
                .orderByDesc(Student::getId)
                .last("limit 1")
                .one();
        System.out.println("one = " + one);
    }
}
