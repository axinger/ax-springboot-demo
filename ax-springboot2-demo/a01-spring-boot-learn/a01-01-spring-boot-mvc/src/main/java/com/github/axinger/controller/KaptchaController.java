package com.github.axinger.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/kaptcha")
public class KaptchaController {

//    @Autowired
//    private DefaultKaptcha captchaProducer;
//
//    @GetMapping("/code/create")
//    public void create(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
//        byte[] captchaOutputStream = null;
//        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
//        try {
//            //生产验证码字符串并保存到session中
//            String verifyCode = captchaProducer.createText();
//            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
//            BufferedImage challenge = captchaProducer.createImage(verifyCode);
//            ImageIO.write(challenge, "jpg", imgOutputStream);
//        } catch (IllegalArgumentException e) {
//            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
//            return;
//        }
//        captchaOutputStream = imgOutputStream.toByteArray();
//        httpServletResponse.setHeader("Cache-Control", "no-store");
//        httpServletResponse.setHeader("Pragma", "no-cache");
//        httpServletResponse.setDateHeader("Expires", 0);
//        httpServletResponse.setContentType("image/jpeg");
//        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
//        responseOutputStream.write(captchaOutputStream);
//        responseOutputStream.flush();
//        responseOutputStream.close();
//    }


    @GetMapping("/code/create")
    public void create(HttpSession session, HttpServletResponse response) throws Exception {
        // 生成验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 80, 4, 5);
        // 存入 session，用于后续验证
        session.setAttribute("captcha", captcha.getCode());
        // 设置响应头
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/png");
        // 输出图片到响应流
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            captcha.write(outputStream);
        }
    }

    @GetMapping("/code/verify")
    public Map<String, Object> verify(@RequestParam("verifyCode") String verifyCode, HttpSession session) {
        String realCode = (String) session.getAttribute("captcha");
        if (StrUtil.equalsIgnoreCase(realCode, verifyCode)) {
            return Map.of("captchaCode", "成功" + verifyCode);
        }
        return Map.of("captchaCode", "过期" + verifyCode);
    }
}
