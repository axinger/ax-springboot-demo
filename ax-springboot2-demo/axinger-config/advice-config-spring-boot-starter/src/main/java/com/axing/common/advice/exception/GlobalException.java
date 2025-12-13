package com.axing.common.advice.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.axing.common.advice.bean.AdviceProperties;
import com.axing.common.response.dto.Result;
import com.axing.common.response.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xing
 * ${axing.response.base-packages:} 有问题
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalException {

    private final AdviceProperties adviceProperties;

    @ExceptionHandler(value = Exception.class)
    public Result<Map<String, Object>> exception(HttpServletRequest request, Exception e) {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        Map<String, Object> map = new HashMap<>(16);
        map.put("method", method);
        map.put("path", uri);
        ///  错误message,没有类型
        String msg = ExceptionUtil.getSimpleMessage(e);
        final Result<Map<String, Object>> result = Result.fail(msg);
        result.setParams(map);
        ///  getRootCauseMessage: 错误类型
        log.error("全局异常,result = {}", JSONObject.toJSONString(result));

        if (adviceProperties.isPrintStackTrace()) {
            log.error("全局异常,stacktrace = {}", ExceptionUtil.stacktraceToString(e));
        }

        return result;
    }

    @ExceptionHandler(value = ServiceException.class)
    public Result<Map<String, Object>> serviceException(ServiceException e) {
//        if (adviceProperties.isPrintStackTrace()) {
//            e.printStackTrace();
//        }

        final Result<Map<String, Object>> result = Result.fail(e.getMessage());
        log.error("自定义业务异常 result =  {}", result);
        return result;
    }

    /**
     * 对方法参数校验异常处理方法
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class,})
    public Result<Object> handlerMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        // 缺少参数异常
        Result<Object> result = Result.fail(StrUtil.format("缺少类型为{}的参数{}", e.getParameterType(), e.getParameterName()));
        log.error("缺少参数异常 result =  {}", result);
        return result;
    }

    /**
     * 对方法参数校验异常处理方法
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public Result<Object> handlerMethodArgumentNotValidException(Exception e) {
//        if (adviceProperties.isPrintStackTrace()) {
//            e.printStackTrace();
//        }
        List<ObjectError> list = new ArrayList<>();

        if (e instanceof MethodArgumentNotValidException validException) {
            list = Optional.of(validException)
                    .map(MethodArgumentNotValidException::getBindingResult)
                    .map(BindingResult::getAllErrors)
                    .orElse(new ArrayList<>());
        } else if (e instanceof BindException bindException) {
            list = Optional.of(bindException)
                    .map(BindingResult::getAllErrors)
                    .orElse(new ArrayList<>());
        }

        Map<String, Object> map = list.stream().collect(Collectors.toMap(error -> {
            if (error instanceof FieldError error1) {
                // 获取错误验证字段名
                return error1.getField();
            } else {
                // 非字段错误
                // 获取验证对象名称
                return error.getObjectName();
            }
        }, error -> Optional.of(error).map(ObjectError::getDefaultMessage).orElse(""), (key1, key2) -> key2));

        // final Result<Map<String, Object>> result = Result.build(201, map, "参数校验异常");
        Result<Object> result = Result.fail(map.toString());
        log.error("方法参数校验异常 result =  {}", result);
        return result;
    }

    /**
     * get 请求参数校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handlerConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        Map<String, Object> map = set.stream()
                .collect(Collectors.toMap(val -> {

                            val.getPropertyPath().iterator();

                            return ((PathImpl) val.getPropertyPath()).getLeafNode().getName();
                        },
                        ConstraintViolation::getMessage, (key1, key2) -> key2));
        Result<Object> result = Result.fail(map.toString());
        log.error("请求参数校验异常 result =  {}", result);
        return result;
    }
}
