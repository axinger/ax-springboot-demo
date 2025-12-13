package com.github.axinger.controller;

import com.github.axinger.db.master.domain.Person;
import com.github.axinger.db.master.domain.SysAnimalEntity;
import com.github.axinger.db.master.service.PersonService;
import com.github.axinger.db.master.service.SysAnimalService;
import com.github.axinger.db.slave.domain.Dog;
import com.github.axinger.db.slave.service.DogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Service
@AllArgsConstructor
public class TestService {


    @Qualifier(value = "masterTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private PersonService personService;
    @Autowired
    private DogService dogService;
    @Autowired
    private SysAnimalService animalService;
    @Autowired
    private TestService2 testService2;
    @Autowired
    private DefaultTransactionDefinition def;

    @Transactional(value = "masterTransactionManager", rollbackFor = Exception.class)
    public void testAB(boolean error) {
        testPerson();
        testSysAnimalEntity(error);
    }

    @Transactional
    public void testAC(boolean error) {
        testPerson();
        testDog(error);
    }

    public void testPerson() {
        System.out.println("Person==========");
        Person dog = new Person();
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


        Dog dog = new Dog();
        dog.setName("加菲狗");
        if (error) {
            int i = 1 / 0;
        }
        dogService.save(dog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void asyncMethod(boolean params) {
        log.info("asyncMethod={}", Thread.currentThread().getName());

        final boolean[] result = {true};
        Void join = CompletableFuture.allOf(
                testService2.asyncMethod1()
                        .exceptionally(val -> {
                            return false;
                        })
                        .whenComplete((r, t) -> {
                            if (t != null) {
                                return;
                            }

                            result[0] = result[0] && r;
                        }),

                testService2.asyncMethod2(params)
                        .exceptionally(val -> {
                            return false;
                        })
                        .whenComplete((r, t) -> {
                            if (t != null) {
                                return;
                            }
                            result[0] = result[0] && r;
                        })
        ).join();


        log.info("(结果asyncMethod)={},result={}", Thread.currentThread().getName(), result[0]);
    }

    /**
     * 1 循环28次自动事务一个个提交耗时1230，而事务集合需要1524，手动事务更加耗时
     * 2 duild配置maxactive大小由Max_used_connections来决定，默认配置20
     * 3 如果使用手动同步计数器事务集合整体提交连接数不超过10个，超时时间不超过50秒
     * 4 使用事务集合最大连接数不能超过maxactive-1否则死锁
     */
    @Async("releaseBatchOrderExecutor")
    public void testTranslation() {
        StopWatch sw = new StopWatch();
        sw.start();
        List<HashMap<TransactionStatus, Boolean>> maps = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(28);
        for (int i = 1; i <= 28; i++) {
            try {
                testService2.testTranslation(i, maps, dataSourceTransactionManager, def, countDownLatch);
            } catch (Exception e) {
                log.info("异常了：" + e.getMessage());
                continue;
            }
        }
//        try {
//            countDownLatch.await();
//            for (HashMap<TransactionStatus, Boolean> item : maps) {
//                for (Map.Entry<TransactionStatus, Boolean> entry : item.entrySet()) {
//                    if (entry.getValue()) {//有异常
//                        dataSourceTransactionManager.rollback(entry.getKey());
//                    } else {
//                        dataSourceTransactionManager.commit(entry.getKey());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            for (HashMap<TransactionStatus, Boolean> item : maps) {
//                for (Map.Entry<TransactionStatus, Boolean> entry : item.entrySet()) {
//                    dataSourceTransactionManager.rollback(entry.getKey());
//                }
//            }
//        }

        sw.stop();
        System.out.println("耗时：" + sw.getTotalTimeMillis());
    }
}
