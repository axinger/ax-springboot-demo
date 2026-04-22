package com.axing.common.advice.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.axing.common.advice.bean.AdviceProperties;
import com.axing.common.response.dto.Result;
import com.axing.common.response.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        log.error("全局异常,result = {}", result);

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
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String requestInfo = getCurrentRequestInfo();
        Result<Object> result = Result.fail(getObjectError(e.getBindingResult().getAllErrors()).toString());
        log.debug("方法参数校验异常, 请求路径: {}, MethodArgumentNotValidException={}", requestInfo, result);
        return result;
    }

    @ExceptionHandler({BindException.class})
    public Result<?> handlerBindException(BindException e) {
        String requestInfo = getCurrentRequestInfo();
        Result<Object> result = Result.fail(getObjectError(e.getAllErrors()).toString());
        log.debug("方法参数校验异常, 请求路径: {}, BindException={}", requestInfo, result);
        return result;
    }

    public Map<String, List<String>> getObjectError(List<ObjectError> list) {
        return list.stream()
                .collect(Collectors.groupingBy(
                        error -> error instanceof FieldError
                                ? ((FieldError) error).getField()
                                : error.getObjectName(),
                        Collectors.mapping(ObjectError::getDefaultMessage, Collectors.toList())
                ));
    }

    /**
     * get 请求参数校验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handlerConstraintViolationException(ConstraintViolationException e) {
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String requestInfo = getCurrentRequestInfo();
        String errorMessage = StrUtil.format("请求路径{},不支持{}方法", requestInfo, e.getMethod());
        log.warn(errorMessage);
        return Result.fail(405, errorMessage);
    }

    /**
     * 获取当前请求信息
     */
    private String getCurrentRequestInfo() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String requestUri = "";
        String queryString = "";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            requestUri = request.getRequestURI();
            queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        }
        return requestUri + queryString;
    }
}
