package com.github.axinger.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义错误处理,过滤器不能被RestControllerAdvice,需要重写ErrorController
 */
@Controller
public class ErrorControllerImpl implements ErrorController {
    // 转发拦截器,过滤器异常
    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) throws Throwable {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            throw (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
    }
}
