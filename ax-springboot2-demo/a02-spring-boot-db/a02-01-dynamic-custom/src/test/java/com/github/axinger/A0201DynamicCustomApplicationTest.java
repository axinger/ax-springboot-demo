package com.github.axinger;

import com.github.db1.domain.SysPersonEntity;
import com.github.db1.service.SysPersonService;
import com.github.db2.domain.SysAnimalEntity;
import com.github.db2.service.SysAnimalService;
import com.github.db21.domain.SysDogEntity;
import com.github.db21.service.SysDogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class A0201DynamicCustomApplicationTest {


    @Autowired
    private SysPersonService personService;

    @Autowired
    private SysDogService dogService;

    @Autowired
    private SysAnimalService animalService;


    @Test
    public void test1() {
        List<SysPersonEntity> list = personService.list();
        System.out.println("list = " + list);
    }

    @Test
    public void test2() {
        List<SysDogEntity> list = dogService.list();
        System.out.println("list = " + list);
    }


    @Test
    public void test3() {
        List<SysAnimalEntity> list = animalService.list();
        System.out.println("list = " + list);
    }
}
