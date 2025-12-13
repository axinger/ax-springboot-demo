package com.github.axinger.controller;

import com.axing.common.excel.ExcelUtils;
import com.github.axinger.model.SysBookDTO;
import com.github.axinger.service.CommonExcelHandler;
import com.github.axinger.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class TestController {
    @Autowired
    ExcelService excelService;


    @GetMapping(value = "/test1")
    public void excel(HttpServletResponse response) throws Exception {
        excelService.exportExcel(response);
    }

    @GetMapping(value = "/test2")
    public void test2(HttpServletResponse response) {

        List<SysBookDTO> list = new ArrayList<>();
        list.add(new SysBookDTO(1L, "十万个为什么", "jim", 1, 1, new Date(), new Date(), "1"));
        list.add(new SysBookDTO(2L, "海底世界", "jim", 1, 1, new Date(), new Date(), "1"));
        list.add(new SysBookDTO(3L, "海底世界", "jim", 1, 1, new Date(), new Date(), "1"));
        list.add(new SysBookDTO(4L, "海底世界", "jim", 1, 1, new Date(), new Date(), "1"));

        ExcelUtils.writeExcel(response, SysBookDTO.class, "123", "456", list);
    }

    @PostMapping(value = "/test3")
    public void test3(@RequestParam("file") MultipartFile file) {

        ExcelUtils.readExcel(file, SysBookDTO.class, new CommonExcelHandler<>(dto -> {
            System.out.println("dto = " + dto);
        }));
    }

}
