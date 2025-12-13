package com.github.axinger.controller;

import com.github.axinger.domain.SysAnimalEntity;
import com.github.axinger.mapper.SysAnimalMapper;
import com.github.axinger.service.SysAnimalService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TestService1 {


    @Autowired
    private Executor executor;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    @Autowired
    private SysAnimalService sysAnimalService;
    @Autowired
    private SysAnimalMapper sysAnimalMapper;

    /**
     * 测试多线程事务.
     */

    /// 正常方式,不是异步, 回滚后,自增id会被占用,回滚的事务会导致自增 ID 出现跳号现象。
    @Transactional
    public void test1() {

        for (int i = 0; i < 20; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "=========" + i);
            SysAnimalEntity entity = new SysAnimalEntity();
            entity.setName("狗" + i);
            if (i == 19) {
                int a = 1 / 0;
            }
            sysAnimalService.save(entity);
        }
    }

    /// 通过 Connection 使用同一个连接,可以回滚, 回滚后,自增id会被占用,回滚的事务会导致自增 ID 出现跳号现象。
    public void test2() throws SQLException {
        SysAnimalMapper baseMapper = (SysAnimalMapper) sysAnimalService.getBaseMapper();
        baseMapper.truncate(); // 无法回滚

//            sysAnimalMapper.deleteById(1); // 可以被回滚

        // 获取数据库连接,获取会话(内部自有事务)
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SqlSession sqlSession = sqlSessionFactory.openSession(false); // false 无效
//        Connection connection = sqlSession.getConnection();
        try {
            // 设置手动提交
//            connection.setAutoCommit(false);
            sqlSession.getConnection().setAutoCommit(false);
            List<CompletableFuture<Void>> futureList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                int finalI = i;
                futureList.add(CompletableFuture.runAsync(() -> {
                    insert(sqlSession, finalI);
                }, executor));
            }
            //执行子线程
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
//            connection.commit();
            sqlSession.commit();
            stopWatch.stop();
            log.info("成功耗时={}秒", stopWatch.getTotalTimeSeconds());
        } catch (Exception e) {
            log.error("总rollback============={}", e.getMessage(), e);
//            connection.rollback();
            sqlSession.rollback(); // 无法回滚
            stopWatch.stop();
            log.info("异常耗时={}秒", stopWatch.getTotalTimeSeconds());
        } finally {
//            connection.close();
            sqlSession.close();
        }
    }

    @SneakyThrows
    private void insert(SqlSession sqlSession, int index) {

        TimeUnit.SECONDS.sleep(1);
        log.info("子线程={}", Thread.currentThread().getName());
        SysAnimalMapper sysAnimalMapper = sqlSession.getMapper(SysAnimalMapper.class);
        SysAnimalEntity entity = new SysAnimalEntity();
        entity.setName("狗" + index);
        if (index == 18) {
            throw new RuntimeException("模拟第18条失败！");
        }
        sysAnimalMapper.insert(entity);
    }

    /// 不能回滚
    @Transactional
    public void test4() {

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // 启动多个线程进行数据库操作
        for (int i = 0; i < 10; i++) {
            final int index = i;
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    performDatabaseOperation(index);  // 这里调用方法执行数据库操作
                } catch (Exception e) {
                    throw new RuntimeException("Database operation failed", e);
                }
            }, executor));
        }

        // 等待所有线程执行完毕
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 如果没有异常，事务会自动提交
    }

    private void performDatabaseOperation(int index) {
        // 模拟数据库插入操作
        SysAnimalEntity entity = new SysAnimalEntity();
        entity.setName("狗" + index);
        sysAnimalMapper.insert(entity);  // 插入数据
    }

}

