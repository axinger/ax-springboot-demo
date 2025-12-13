package com.github.axinger;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.sys.domain.SysUserEntity;
import org.junit.jupiter.api.Test;

public class PagesTests {
    @Test
    void test1() {
        Page<Object> objectPage = Page.of(2, 3, 20);

        System.out.println("objectPage = " + objectPage);
    }

    @Test
    void test2() {
        Page<Object> objectPage = Page.of(1, 10, true);
        Long l = objectPage.maxLimit();
        System.out.println("l = " + l);

        System.out.println("objectPage = " + objectPage);

        Page<String> objectPage1 = Page.of(2, 3, 10);
        System.out.println("objectPage1 = " + objectPage1);

        PageDTO<String> pageDTO = new PageDTO<>(2, 3, 10, true);
        System.out.println("pageDTO.getCurrent() = " + pageDTO.getCurrent());
        System.out.println("pageDTO.getSize() = " + pageDTO.getSize());
        System.out.println("pageDTO.getPages() = " + pageDTO.getPages());
        System.out.println("pageDTO.getMaxLimit() = " + pageDTO.getMaxLimit());
    }

    @Test
    void test3() throws JsonProcessingException {
        IPage<SysUserEntity> page = Page.of(4, 3, 10, true);
        Long l = page.maxLimit();
        System.out.println("l = " + l);

        ObjectMapper objectMapper = new ObjectMapper();

        String s = objectMapper.writeValueAsString(page);
        System.out.println("s = " + s);

        TypeReference<Page<SysUserEntity>> typeReference = new TypeReference<>() {
        };

        IPage<SysUserEntity> page1 = objectMapper.readValue(s, typeReference);
        System.out.println("page1 = " + page1);


    }
}
