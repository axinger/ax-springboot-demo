package com.github.axinger.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.github.axinger.service.SecondsKillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SecondsKillServiceImpl implements SecondsKillService {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // @Autowired
    // private Redisson redisson;

    @Autowired
    private RedissonClient redisson;


    String prodKey(Integer prodId) {
        // 库存key
        String prodKey = "sk:" + prodId + ":qt:int";
        return prodKey;
    }

    String userKilledKey(Integer prodId) {
        return "sk:" + prodId + ":user:set";
    }

    @Override
    public void createProductCount(Integer prodId) {
        String prodKey = prodKey(prodId);
        redisTemplate.opsForValue().set(prodKey, "10");
        Integer count = productCount(prodId);
        log.info("初始化库存 = {}", count);
    }

    @Override
    public Integer productCount(Integer prodId) {
        String prodKey = prodKey(prodId);
        Object o = redisTemplate.opsForValue().get(prodKey);
        return Integer.getInteger(String.valueOf(o));
    }

    /**
     * 秒杀,并发
     */
    @Override
    public boolean doSecKill(Integer userId, Integer prodId) {
        // 库存key
        String prodKey = prodKey(prodId);
        log.info("库存key = " + prodKey);
        // 商品已经秒杀过的 用户集合key
        String userKilledKey = userKilledKey(prodId);
        log.info("用户集合key  = " + userKilledKey);

        /// 监视库存,并发
        redisTemplate.watch(prodKey);
        // 判断库存是否存在
        if (redisTemplate.opsForValue().get(prodKey) == null) {
            log.info("秒杀还未开始.........");
            return false;
        }

        // 判断用户
        Boolean member = redisTemplate.opsForSet().isMember(userKilledKey, userId);
        if (member) {
            log.info("已经秒杀过了.........");
            return false;
        }

        // 判断库存
        if ((Integer) redisTemplate.opsForValue().get(prodKey) <= 0) {

            log.info("库存为0,活动结束.........");
            return false;
        }
        // 事务,组队
//        redisTemplate.multi();
//        redisTemplate.opsForValue().decrement(prodKey);
//        redisTemplate.opsForSet().add(userKilledKey, userId);
//        // 事务,执行
//        List<Object> exec = redisTemplate.exec();
////        redisTemplate.unwatch();
//        if (exec == null || exec.size()==0){
//            log.info("秒杀失败........."+userId);
//            return false;
//        }


        redisTemplate.execute((RedisCallback<Object>) (redisOperations) -> {

            // 开启事务支持，在同一个 Connection 中执行命令
            redisTemplate.setEnableTransactionSupport(true);
            redisOperations.multi();
            redisTemplate.opsForValue().decrement(prodKey);
            redisTemplate.opsForSet().add(userKilledKey, userId.toString());
            return redisOperations.exec();
        });


        log.info("秒杀成功一次........." + userId);
        return true;
    }

    @Override
    public boolean doSecKillOfRedissonLock(Integer userId, Integer prodId) {

        RLock phoneLock = redisson.getLock("phoneLock");
        phoneLock.lock(3, TimeUnit.SECONDS);
        try {
            // 库存key
            String prodKey = prodKey(prodId);
            // 商品已经秒杀过的 用户集合key
            String userKilledKey = userKilledKey(prodId);
            // 判断库存是否存在
            Object prodValue = redisTemplate.opsForValue().get(prodKey);

            if (ObjUtil.isEmpty(prodValue)) {
                log.info("秒杀还未开始.........");
                return false;
            }
            Integer count = (Integer) prodValue;
            // 判断库存
            if (count <= 0) {
                log.info("库存为0,活动结束: {}", prodValue);
                return false;
            }
            // 判断用户
            Boolean member = redisTemplate.opsForSet().isMember(userKilledKey, userId);
            if (member) {
                log.info("已经秒杀过了.........");
                return false;
            }
            Long decrement = redisTemplate.opsForValue().decrement(prodKey);
            log.info("秒杀成功 {} 抢到了 {} 商品,剩余商品个数 {}", userId, prodValue, decrement);
            Long add = redisTemplate.opsForSet().add(userKilledKey, userId);
            log.info("userKilledKey add = {}", add);

            return true;
        } finally {
            phoneLock.unlock();
        }
    }

    /**
     * 添加秒杀产品库存
     *
     * @param prodId
     * @return
     */
    @Override
    public Integer addSk(Integer prodId) {
        // 库存key
        String prodKey = prodKey(prodId);
        // 判断库存是否存在
        Object o = redisTemplate.opsForValue().get(prodKey);
        if (ObjUtil.isEmpty(o) || ObjUtil.equal(o, 0)) {
            redisTemplate.opsForValue().set(prodKey, 100);
            String userKilledKey = userKilledKey(prodId);
            Boolean delete = redisTemplate.delete(userKilledKey);
            log.info("userKilledKey delete = {}", delete);

        }
        Integer count = (Integer) redisTemplate.opsForValue().get(prodKey);
        return count;
    }


    @Override
    public Map<String, Object> killExecute() {

        try {

            Integer prodId = 101;
            Integer sk = this.addSk(prodId);

            List<CompletableFuture> futureList = new ArrayList<>();
            Set<Object> successSet = new HashSet<>();

            for (int i = 0; i < 300; i++) {
                futureList.add(CompletableFuture.runAsync(() -> {
                    Integer userId = new Random().nextInt(300);
                    boolean secKill = this.doSecKillOfRedissonLock(userId, prodId);
                    if (secKill) {
                        successSet.add(userId);
                    } else {
                        log.info("秒杀失败,{} 未抢到商品", userId);
                    }
                }));
            }

            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

            String userKilledKey = userKilledKey(prodId);
            Long size = redisTemplate.opsForSet().size(userKilledKey);

            String prodKey = prodKey(prodId);
            Object lastSk = redisTemplate.opsForValue().get(prodKey);


            Map<String, Object> map = new HashMap();
            map.put("prodId", prodId);
            map.put("sk", sk);
            map.put("lastSk", lastSk);
            map.put("successList", successSet.size());
            map.put("successSet", size);

            return map;

        } catch (Exception e) {
            log.error("多线程秒杀失败 = {}", e.getMessage());
            return null;
        }
    }
}
