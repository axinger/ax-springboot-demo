package com.github.axinger.controller;


import com.github.axinger.dto.PaymentApiDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author xing
 */
@RequestMapping("/payment4")
@RestController
public class Payment4Controller {

    @Value("${server.port}")
    private String port;


    @PostMapping(value = "/login")
    public PaymentApiDTO.LoginVO login(@RequestBody PaymentApiDTO.LoginDTO dto) {
        PaymentApiDTO.LoginVO vo = new PaymentApiDTO.LoginVO();
        vo.setToken("123");
        return vo;
    }


    @GetMapping(value = "/payment")
    public PaymentApiDTO.PaymentVO payment(PaymentApiDTO.PaymentDTO dto) {

        PaymentApiDTO.PaymentVO paymentVO = new PaymentApiDTO.PaymentVO();
        paymentVO.setOrderId(dto.getOrderId() + ":" + port);
        return paymentVO;
    }

}
