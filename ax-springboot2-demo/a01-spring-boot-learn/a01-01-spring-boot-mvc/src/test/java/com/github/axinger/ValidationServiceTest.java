package com.github.axinger;

import cn.hutool.core.date.DateUtil;
import com.github.axinger.model.dto.DateDTO;
import com.github.axinger.model.dto.LoginDTO;
import com.github.axinger.model.dto.ParamDTO;
import com.github.axinger.validation.NotEmptyService;
import com.github.axinger.validation.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;


@SpringBootTest
public class ValidationServiceTest {

    @Autowired
    ValidationService validationService;
//    ValidationServiceImpl validationService;

    @Autowired
    private NotEmptyService notEmptyService;

    @Test
    void test1() {
        validationService.strNo(null);
    }

    @Test
    void strNotBlank_1() {
        validationService.strNotBlank(null); // 能被校验
    }

    @Test
    void strNotBlank_12() {
        validationService.strNotBlank("123");
    }

    @Test
    void strNotBlank_13() {
        validationService.strNotNull3("");
    }

    @Test
    void strNotBlank_2() {
        validationService.strNotBlank(""); // 能被校验,不通过
    }

    @Test
    void strNotBlank_3() {
        validationService.strNotBlank(" "); //能被校验,不通过
    }

    @Test
    void strNotEmpty_1() {
        notEmptyService.strNotEmpty(null); // 能被校验,不通过
    }

    @Test
    void strNotEmpty_2() {
        //@NotEmpty
        notEmptyService.strNotEmpty(""); // 能被校验,不通过
    }

    @Test
    void strNotEmpty_3() {
        //@NotEmpty
        notEmptyService.strNotEmpty(" "); //不能被校验
    }


    @Test
    void strNotEmpty_4() {

//        validationService.strNotNull(""); // 通过
//        validationService.strNotNull(null); // 能被校验,不通过

//        validationService.listNotEmpty(null);// 能被校验,不通过
//        validationService.listNotEmpty(new ArrayList<>());// 能被校验,不通过
        validationService.listNotEmpty(new ArrayList<>() {{
            add("1");
        }});// 通过

    }

    @Test
    void funOne_1() {
        ParamDTO dto = new ParamDTO();
        validationService.funOne(dto);
    }

    @Test
    void funOne_2() {
        ParamDTO dto = new ParamDTO();
        dto.setName("jim");
        validationService.funOne(dto);
    }

    @Test
    void funOne_3() {
        ParamDTO dto = new ParamDTO();
        dto.setDate(new Date());
        validationService.funOne(dto);
    }

    @Test
    void funOne_4() {
        ParamDTO dto = new ParamDTO();
        dto.setName("jim");
        dto.setDate(new Date());
        validationService.funOne(dto);
    }


    @Test
    void test2() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        validationService.loginDTO(loginDTO); //能被校验
    }

    @Test
    void test22() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        validationService.loginDTO2(loginDTO);
    }

    @Test
    void test23() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        validationService.loginDTO3(loginDTO);
    }

    @Test
    void test24() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        validationService.loginDTO4(loginDTO);
    }

    @Test
    void test25() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        validationService.loginDTO5(loginDTO);
    }


//    @Test
//    void testDate_1(){
//        DateDTO dto = new DateDTO();
//        dto.setStart(LocalDateTime.now());
//        dto.setEnd(LocalDateTime.now());
//        validationService.testDate(dto);
//    }
//    @Test
//    void testDate_2(){
//        DateDTO dto = new DateDTO();
//        dto.setStart(LocalDateTime.now());
//        dto.setEnd(LocalDateTime.now().plusDays(-1));
//        validationService.testDate(dto);
//    }

    @Test
    void testDate_3() {
        DateDTO dto = new DateDTO();
        dto.setBeginTime(DateUtil.parse("2020-01-02 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        dto.setEndTime(DateUtil.parse("2020-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        validationService.testDate(dto);
    }

    @Test
    void testDate_4() {
        DateDTO dto = new DateDTO();
        dto.setBeginTime(DateUtil.parse("2020-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        dto.setEndTime(DateUtil.parse("2020-01-02 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        validationService.testDate(dto);
    }


}
