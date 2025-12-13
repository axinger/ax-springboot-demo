package com.github.axinger;

import com.github.axinger.dao.SysUsersDAO;
import com.github.axinger.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

@SpringBootTest
public class JpaDemo1Tests {

    // JpaRepository
    @Autowired
    private SysUsersDAO sysUsersDao;

    @Test
    void test01() {
        SysUser book = new SysUser();
        book.setId(1L);
        book.setUsername("张三");
        LocalDateTime dateTime = LocalDateTime.now().plusDays(-1);
        book.setCreateDate(dateTime);
        book.setUpdateDate(dateTime.plusHours(2));
        sysUsersDao.save(book);
    }


    void test02() {
        SysUser book = new SysUser();
        book.setId(1L);
        book.setUsername("张三");
        LocalDateTime dateTime = LocalDateTime.now().plusDays(-1);
        book.setCreateDate(dateTime);
        book.setUpdateDate(dateTime.plusHours(2));
        sysUsersDao.saveAndFlush(book);
    }

    void test03() {

        Page<SysUser> people = sysUsersDao.findByAgeGreaterThan(9, PageRequest.of(0, 10));
        System.out.println("people = " + people.getContent());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<SysUser> page = sysUsersDao.findAll(pageable);
    }
}
