package com.github.axinger;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.axinger.sys.domain.SysUserEntity;
import com.github.axinger.sys.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@SpringBootTest
public class SysUserServiceTest {


    @Autowired
    private SysUserService sysUserService;


    @Test
    void getSysUserById() {
        List<SysUserEntity> list = sysUserService.list();
        System.out.println("list = " + list);
    }

    @Test
    void test_update1() {
        SysUserEntity person = new SysUserEntity();
        sysUserService.lambdaUpdate()
                .eq(SysUserEntity::getId, 1)
                .setSql("age = age+1")
                .update(person);
    }

    @Test
    void test_update2() {
        sysUserService.lambdaUpdate()
                .eq(SysUserEntity::getId, 1)
                .setSql("age = age+1")
                .update();
    }

    @Test
    void test2() {
        long count = sysUserService.count();

        int pageSize = 3;
        int totalPage = PageUtil.totalPage(count, pageSize);

        for (int i = 0; i <= totalPage; i++) {

            System.out.println("limit " + i + "," + pageSize);
            IPage<SysUserEntity> page = Page.of(i, pageSize);
            IPage<SysUserEntity> res = sysUserService.lambdaQuery()
                    .page(page);

            System.out.println("res.getRecords() = " + res.getRecords().stream().map(SysUserEntity::getId).toList());
        }


    }

    @Test
    void test3() {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 1000; i++) {
                sysUserService.lambdaUpdate()
                        .eq(SysUserEntity::getId, 1)
                        .set(SysUserEntity::getAge, i)
                        .update();
            }
        });

        CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 1000; i++) {
                sysUserService.lambdaUpdate()
                        .eq(SysUserEntity::getId, 1)
                        .set(SysUserEntity::getAge, i)
                        .update();
            }
        });
        voidCompletableFuture.join();
    }

}
