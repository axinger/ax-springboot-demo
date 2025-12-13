package com.github.axinger.controller;

import com.github.axinger.domain.SysAnimalEntity;
import com.github.axinger.mapper.SysAnimalMapper;
import com.github.axinger.service.SysAnimalService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@AllArgsConstructor
public class TestService2 {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private Executor executor;

    @Autowired
    private SysAnimalMapper sysAnimalMapper;

    @Autowired
    private SysAnimalService sysAnimalService;

    @Resource
    private MultiplyThreadTransactionManager multiplyThreadTransactionManager;


    @SneakyThrows
    public void test() {

        //子线程中是否有异常标识
        AtomicBoolean isError = new AtomicBoolean(false);

        List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            int finalI = i;

            tasks.add(() -> {
                saveSysUserAddressByTransaMan(finalI);
            });
        }

        multiplyThreadTransactionManager.runAsyncButWaitUntilAllDown(tasks, executor);
    }


    public void saveSysUserAddressByTransaMan(int id) {

        SysAnimalEntity animal = new SysAnimalEntity();
        animal.setName("name" + id);
        sysAnimalMapper.insert(animal);

        System.out.println("子线程：" + Thread.currentThread().getName());
    }


}


