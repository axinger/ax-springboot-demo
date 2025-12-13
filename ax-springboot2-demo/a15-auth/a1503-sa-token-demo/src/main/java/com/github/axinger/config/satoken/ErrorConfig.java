package com.github.axinger.config.satoken;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.axing.common.response.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ResponseBody
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorConfig {

    @ExceptionHandler(value = SaTokenException.class)
    public Result<Map<String, Object>> saTokenException(HttpServletRequest request, Exception e) {
        String uri = request.getRequestURI();
        Map<String, Object> map = new HashMap<>(16);
        map.put("method", request.getMethod());
        map.put("path", uri);
        ///  错误message,没有类型
        String msg = ExceptionUtil.getSimpleMessage(e);
        final Result<Map<String, Object>> result = Result.fail(msg);
        result.setParams(map);
        ///  getRootCauseMessage: 错误类型
        log.error("SaTokenException异常,result = {}", JSONObject.toJSONString(result));
        return result;
    }
}
