package com.xing;

import com.xing.entity.Dog;
import com.xing.entity.Person;
import com.xing.repository.DogRepository;
import com.xing.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
@Slf4j
class RepositoryTests {


    @Autowired
    private PersonRepository personRepository;


    @Autowired
    private DogRepository dogRepository;


    @Test
    void save() {
        Person person = new Person();
        person.setName("jim");
        person.setAge(10);
        final Person save = personRepository.save(person);
        System.out.println("save = " + save);

        Dog dog = new Dog();
        dog.setName("golf");
        dog.setAge(9);
        dogRepository.save(dog);
    }

    @Test
    void findAll() {

        final List<Person> all = personRepository.findAll();
        System.out.println("all = " + all);
    }

    @Test
    void findAllPage() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        final Page<Person> personPage = personRepository.findAll(pageRequest);

        System.out.println("personPage = " + personPage);
        System.out.println("personPage.getContent() = " + personPage.getContent());
    }

    @Test
    void findAllName() {
        final Person name = personRepository.getByName("jim");
        System.out.println("name = " + name);
    }

    @Test
    void findAllListName() {
        final List<Person> list = personRepository.findListByName("jim");
        System.out.println("list = " + list);
    }

    @Test
    void findAllLike() {
        final List<Person> list = personRepository.findByNameLike("jim");
        System.out.println("list = " + list);
    }

    @Test
    void findAll_Sort() {
        List<Person> list = personRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        System.out.println("list = " + list);
    }

}
