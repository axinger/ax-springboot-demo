package com.github.axinger.controller;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.github.axinger.annotation.DB1;
import com.github.axinger.db1.domain.SysPersonEntity;
import com.github.axinger.db1.service.SysPersonService;
import com.github.axinger.db2.domain.SysAnimalEntity;
import com.github.axinger.db2.domain.SysDogEntity;
import com.github.axinger.db2.service.SysAnimalService;
import com.github.axinger.db2.service.SysDogService;
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


    @DB1
    @DSTransactional
    public void testAB(boolean error) {
        testPerson();
        testSysAnimalEntity(error);
    }

    @DB1
    @DSTransactional
    public void testAC(boolean error) {
        testPerson();
        testDog(error);
    }


    public void testPerson() {
        System.out.println("Person==========");
        SysPersonEntity dog = new SysPersonEntity();
        dog.setName("小明");
        personService.save(dog);
    }


    public void testSysAnimalEntity(boolean error) {
        System.out.println("SysAnimalEntity==========");
        SysAnimalEntity entity = new SysAnimalEntity();
        entity.setName("狗");
        if (error) {
            int i = 1 / 0;
        }
        animalService.save(entity);
    }

    public void testDog(boolean error) {
        SysDogEntity dog = new SysDogEntity();
        dog.setName("加菲狗");
        if (error) {
            int i = 1 / 0;
        }
        dogService.save(dog);
    }


}
