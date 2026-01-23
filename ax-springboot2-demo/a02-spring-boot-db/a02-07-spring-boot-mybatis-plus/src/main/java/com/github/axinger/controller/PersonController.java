package com.github.axinger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.axinger.sys.domain.SysPersonEntity;
import com.github.axinger.sys.mapper.SysPersonMapper;
import com.github.axinger.sys.service.SysPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private SysPersonService sysPersonService;

    @GetMapping("/add")
    public Object add() {
        return sysPersonService.save(SysPersonEntity.builder().name("jim").build());
    }

    @GetMapping("/list")
    public List<SysPersonEntity> list() {
        List<SysPersonEntity> list = sysPersonService.list();
        System.out.println("list = " + list);
        return list;
    }

    @Autowired
    private SysPersonMapper sysPersonMapper;
    @GetMapping("/list2")
    public List<SysPersonEntity> list2() {
//        List<SysPersonEntity> list = sysPersonService.lambdaQuery()
//                .eq(SysPersonEntity::getDeleted, 1)
//                .list();
//


// 使用 mapper 直接查询
//        List<SysPersonEntity> list = sysPersonMapper.selectList(
//                new LambdaQueryWrapper<SysPersonEntity>()
//                        .eq(SysPersonEntity::getDeleted, 1)
//        );

        List<SysPersonEntity> list = sysPersonService.lambdaQuery()
                .apply("1=1")  // 覆盖MyBatis Plus自动添加的逻辑删除条件
                .eq(SysPersonEntity::getDeleted, 1)
                .list();
        System.out.println("list = " + list);
        return list;
    }

    @GetMapping("/list3")
    public List<SysPersonEntity> list3() {
        List<SysPersonEntity> list = sysPersonService.lambdaQuery()
                .eq(SysPersonEntity::getDeleted, 0)
                .list();
        System.out.println("list = " + list);
        return list;
    }

    @GetMapping("/list4")
    public List<SysPersonEntity> list4() {
        List<SysPersonEntity> list = sysPersonService.lambdaQuery()
                .in(SysPersonEntity::getDeleted, 0, 1)
                .list();
        System.out.println("list = " + list);
        return list;
    }


}
