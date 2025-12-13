package com.github.axinger.controller;

import com.github.axinger.db.master.domain.Person;
import com.github.axinger.db.master.domain.SysAnimalEntity;
import com.github.axinger.db.master.service.PersonService;
import com.github.axinger.db.master.service.SysAnimalService;
import com.github.axinger.db.slave.service.DogService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class TestService2 {


    @Autowired
    private PersonService personService;

    @Autowired
    private DogService dogService;

    @Autowired
    private SysAnimalService animalService;


    @Async
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<Boolean> asyncMethod1() {
        log.info("asyncMethod1={}", Thread.currentThread().getName());
        System.out.println("Person==========");
        Person person = new Person();
        person.setName("小明");
        boolean save = personService.save(person);

        return CompletableFuture.completedFuture(save);
    }

    @SneakyThrows
    @Async
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<Boolean> asyncMethod2(boolean error) {

        TimeUnit.SECONDS.sleep(3);
//        log.info("asyncMethod2={}", Thread.currentThread().getName());
//        Dog dog = new Dog();
//        dog.setName("加菲狗");
//        if (error) {
//            int i = 1 / 0;
//        }
//        boolean save = dogService.save(dog);


        SysAnimalEntity entity = new SysAnimalEntity();
        entity.setName("狗");
        if (error) {
            int i = 1 / 0;
        }
        boolean save = animalService.save(entity);

        return CompletableFuture.completedFuture(save);
    }


    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void testTranslation(int i, List<HashMap<TransactionStatus, Boolean>> maps, DataSourceTransactionManager dataSourceTransactionManager, DefaultTransactionDefinition def
            , CountDownLatch countDownLatch) {
//        HashMap<TransactionStatus,Boolean> map=new HashMap<>();
//        //事物隔离级别，开启新事务，这样会比较安全些。
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        //获得事务状态
//        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);

        try {
//            map.put(status,false);
            TimeUnit.SECONDS.sleep(1);
            log.info("当前编号：" + i);
            SysAnimalEntity entity = new SysAnimalEntity();

            entity.setName("狗" + i);
            animalService.save(entity);
            if (i == 1) {
                int s = 4 / 0;
            }
//            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
//            dataSourceTransactionManager.rollback(status);
//            map.put(status,true);
            throw e;
        } finally {
//            countDownLatch.countDown();
//            maps.add(map);
        }
    }

}
