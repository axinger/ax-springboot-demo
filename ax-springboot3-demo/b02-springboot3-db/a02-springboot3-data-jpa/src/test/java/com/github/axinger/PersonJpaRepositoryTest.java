package com.github.axinger;

import com.github.axinger.dao.PersonJpaRepository;
import com.github.axinger.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class PersonJpaRepositoryTest {

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Test
    public void test_01() {

        Page<Person> people = personJpaRepository.findByAgeGreaterThan(9, PageRequest.of(0, 10));
        System.out.println("people = " + people.getContent());

    }

}
