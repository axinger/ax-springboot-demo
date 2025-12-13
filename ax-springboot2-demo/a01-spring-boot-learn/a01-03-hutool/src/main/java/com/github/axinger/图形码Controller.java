package com.github.axinger;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/hutool")
public class 图形码Controller {

    @GetMapping("/code/create")
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 自定义纯数字的验证码（随机4位数字，可重复）
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100);
        captcha.setGenerator(randomGenerator);
        // 重新生成code
        captcha.createCode();

        // 定义图形验证码的长、宽、验证码字符数、干扰线宽度
        // ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(150, 40, 5, 4);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write(response.getOutputStream());
        // 获取验证码中的文字内容
        String verifyCode = captcha.getCode();
        request.getSession().setAttribute("verifyCode", verifyCode);
    }

    @GetMapping("/code/verify")
    public Map<String, Object> verify(@RequestParam("verifyCode") String code,
                                      HttpSession session) {
        String captchaCode = String.valueOf(session.getAttribute("verifyCode"));
        if (code.equals(captchaCode)) {
            return Map.of("captchaCode", "成功" + captchaCode);
        }
        return Map.of("captchaCode", "过期" + captchaCode);
    }
}
