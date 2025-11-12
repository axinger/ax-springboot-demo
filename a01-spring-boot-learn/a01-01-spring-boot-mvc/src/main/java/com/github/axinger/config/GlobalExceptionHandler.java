package com.github.axinger.config;

import cn.hutool.core.util.StrUtil;
import com.axing.common.response.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
//@ResponseBody
@RestControllerAdvice
@RequiredArgsConstructor
//@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统错误: {}", e.getMessage(),e);
        return Result.fail("系统错误: " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return Result.fail("参数校验失败: " + message);
    }

    @ExceptionHandler(org.springframework.validation.BindException.class)
    public Result<Void> handleMethodArgumentNotValidException(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return Result.fail("参数校验失败: " + message);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class,})
    public Result<Object> handlerMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        // 缺少参数异常
        Result<Object> result = Result.fail(StrUtil.format("缺少类型为{}的参数{}", e.getParameterType(), e.getParameterName()));
        log.error("缺少参数异常 result =  {}", result);
        return result;
    }


}
