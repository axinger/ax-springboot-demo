package com.github.axinger.controller;

import com.github.axinger.service.SecondsKillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
@Tag(name = "SecondsKillController", description = "秒杀")
public class SecondsKillController {

    /**
     * 1.库存-1
     * 2. 成功清单+1
     */


    @Autowired
    SecondsKillService secondsKillService;

    @GetMapping(value = "/doSecKill")
    public boolean doSecKill() {

        Integer userId = new Random().nextInt(500);
        Integer prodId = 101;

        return secondsKillService.doSecKill(userId, prodId);
    }

    @GetMapping(value = "/secKill")
    public Object doSecKill2() {
        Integer prodId = 101;
        secondsKillService.createProductCount(prodId);
        ExecutorService service = Executors.newFixedThreadPool(50);
        try {
            for (int i = 0; i < 100; i++) {
                service.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " :办理业务");
                    Integer userId = new Random().nextInt(500);
                    boolean secKill = secondsKillService.doSecKill(userId, prodId);
                    System.out.println("秒杀 secKill = " + secKill);
                });
            }
        } finally {
            /// 关闭
            service.shutdown();
        }
        Integer count = secondsKillService.productCount(prodId);

        Map map = new HashMap();
        map.put("count", count);

        return map;
    }


    @Operation(summary = "redisson锁,解决秒杀")
    @GetMapping(value = "/doSecKillByRedisson")
    public boolean doSecKillByRedisson() {
        // 300个人,抢100个产品
        Integer userId = new Random().nextInt(300);
        Integer prodId = 101;
        return secondsKillService.doSecKillOfRedissonLock(userId, prodId);
    }

    @Operation(summary = "添加秒杀产品库存")
    @GetMapping(value = "/addSk")
    public Integer addSk() {
        Integer prodId = 101;
        Integer sk = secondsKillService.addSk(prodId);
        return sk;
    }

    @Operation(summary = "多线程秒杀产品库存")
    @GetMapping(value = "/doSecKillByRedissonExecute")
    public Object doSecKillByRedissonExecute() {
        return secondsKillService.killExecute();
    }

}
