package com.axing.common.advice.response;

import com.axing.common.advice.bean.AdviceProperties;
import com.axing.common.advice.util.AdviceUtil;
import com.axing.common.response.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/*
  你会发现Swagger3会报Unable to infer base url……的错误，这是因为统一返回体影响到了Swagger3的一些内置接口。
  解决方法是@RestControllerAdvice控制好生效的包范围,也就是配置其basePackages参数就行了，这个潜在的冲突浪费我了一个多小时。
  https://jishuin.proginn.com/p/763bfbd5811f

  @author xing
 */

/**
 * 为了解决swagger-ui拦截
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponse implements ResponseBodyAdvice<Object> {

    private final AdviceProperties adviceProperties;


    /**
     * 判断类或者方法是否使用了 @ResponseResultBody
     *
     * @param returnType    returnType
     * @param converterType converterType
     * @return return
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
//         boolean isSpringdoc = StrUtil.contains(returnType.getDeclaringClass().getName(), "org.springdoc");
//         log.info("isSpringdoc = {}", isSpringdoc);
//         return !isSpringdoc;
        return AdviceUtil.filter(adviceProperties, returnType);
    }


    /**
     * 当类或者方法使用了 @ResponseResultBody 就会调用这个方法
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        if (body instanceof String) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Result.success(body));
        }

        if (body instanceof Resource) {
            return body;
        }

        if (selectedContentType == MediaType.TEXT_HTML) {
            return body;
        }

        /// 防止重复包裹的问题出现
        if (body instanceof Result) {
            return body;
        }

        if (body instanceof Boolean res) {
            return res ? Result.success() : Result.fail();
        }
        return Result.success(body);
    }
}
