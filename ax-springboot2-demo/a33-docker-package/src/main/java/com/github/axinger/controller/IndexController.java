package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import com.github.axinger.dao.SysUsersDAO;
import com.github.axinger.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class IndexController {

    @Value("${config.name}")
    private String name;
    @Autowired
    private SysUsersDAO sysUsersDAO;

    @GetMapping("/data")
    public Result<?> data() {
        log.info("info");
        log.error("error");
        log.warn("warn");
        log.debug("debug");
        log.trace("trace");
        return Result.success(Map.of("name", name, "dateTime", LocalDateTime.now()));
    }

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/")
    public Result<?> index() {
        List<SysUser> list = sysUsersDAO.findAll();
        return Result.success(list);
    }

    @GetMapping("/add")
    public Result<?> add(String username) {

        SysUser sysUser = SysUser.builder()
                .username(username)
                .password("123456")
                .age(18)
                .sex(1).build();
        SysUser saved = sysUsersDAO.save(sysUser);
        return Result.success(saved);
    }
}
