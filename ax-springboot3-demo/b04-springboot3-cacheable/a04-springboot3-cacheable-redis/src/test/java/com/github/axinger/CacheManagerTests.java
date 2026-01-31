package com.github.axinger;

import com.github.axinger.domain.PersonEntity;
import com.github.axinger.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@SpringBootTest
public class CacheManagerTests {

    @Autowired
    private PersonService personService;

//    @Test
//    void findUser() {
//        PersonEntity person = personService.savePersonEntity(1);
//        System.out.println("person = " + person);
//    }

    @Test
    void findUser() {
        PersonEntity person = personService.findPersonEntity(1);
        System.out.println("person = " + person);
    }

    @Test
    void findUser2() {
        PersonEntity person = personService.getById(1);
        System.out.println("person = " + person);

    }

    @Test
    void savePersonNullId() {
        PersonEntity person = new PersonEntity();
        person.setName("jack");
        person.setBirthday(LocalDateTime.now());

        PersonEntity person2 = personService.savePersonNullId(person);
        System.out.println("person = " + person);
        System.out.println("person2 = " + person2);
    }

    @Test
    void updatePersonEntity() {
        PersonEntity person = new PersonEntity();
        person.setId(1L);
        person.setAge(10);

        PersonEntity person2 = personService.updatePersonEntity(person);
        System.out.println("person = " + person);
        System.out.println("person2 = " + person2);

    }

    @Test
    void deletePersonEntity() {
        personService.deletePersonEntity(1);
    }

    @Test
    void savePersonList() {
        List<PersonEntity> list = new ArrayList<>();

        PersonEntity person = new PersonEntity();
        person.setAge(10);
        list.add(person);

        personService.savePersonList(list);
    }

//
//
//    public Object update(Integer id) {
//        User user = User.builder().id(id).name("jim" + new Random().nextInt(100)).age(10).build();
//        return demoUserService.updateUser(user);
//    }
//
//
//    public boolean deleteUser(Integer id) {
//        System.out.println("deleteUser...........");
//        demoUserService.deleteUser(id);
//        return true;
//
//        public Result<?> getLastName (Integer id){
//            System.out.println("getLastName...........");
//            return Result.ok(demoUserService.getLastName(id));
//        }
//
//    }

}
