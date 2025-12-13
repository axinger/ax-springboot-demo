package com.github.axinger.controller;

import com.github.db1.domain.SysPersonEntity;
import com.github.db1.service.SysPersonService;
import com.github.db2.domain.SysAnimalEntity;
import com.github.db2.service.SysAnimalService;
import com.github.db21.domain.SysDogEntity;
import com.github.db21.service.SysDogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TestService {


    @Autowired
    private SysPersonService personService;

    @Autowired
    private SysDogService dogService;

    @Autowired
    private SysAnimalService animalService;

    public void testAB() {
        testA();
        testB();
    }


    public void testAC() {
        testA();
        testC();
    }


    public void testA() {
        System.out.println("Person==========");
        SysPersonEntity dog = new SysPersonEntity();
        dog.setName("小明");
        personService.save(dog);
    }


    public void testB() {
        System.out.println("SysAnimalEntity==========");
        SysAnimalEntity entity = new SysAnimalEntity();
        entity.setName("狗");
//        int i = 1 / 0;
        animalService.save(entity);
    }

    public void testC() {
        SysDogEntity dog = new SysDogEntity();
        dog.setName("加菲狗");
        dogService.save(dog);
    }


}
