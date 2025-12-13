package com.github.axinger;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Objects;


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


    /**
     * 判断类或者方法是否使用了 @ResponseResultBody
     *
     * @param returnType    returnType
     * @param converterType converterType
     * @return return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // boolean isSpringdoc = StrUtil.contains(returnType.getDeclaringClass().getName(), "org.springdoc");
        // log.info("isSpringdoc = {}", isSpringdoc);
        // return !isSpringdoc;
//        return AdviceUtil.filter(adviceProperties, returnType);
        return true;
    }


    /**
     * 当类或者方法使用了 @ResponseResultBody 就会调用这个方法
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {


        System.out.println("body = " + body);

        if (body instanceof Resource) {
            return body;
        }
        if (body instanceof PagedModel<?> pagedModel) {
//            Collection<Object> collection = new ArrayList<>();

            List<?> list = pagedModel.getContent().stream().filter(val -> (val instanceof EntityModel<?>))
                    .map(val -> ((EntityModel<?>) val).getContent()).toList();

//            for (Object o : pagedModel.getContent()) {
//                if (o instanceof EntityModel<?> entityModel) {
//
////                    EntityModel<?> entityModel = res2.removeLinks();
////                    Object content = entityModel.getContent();
//                    Object content = entityModel.getContent();
////                    System.out.println("content = " + content);
//                    collection.add(content);
//                }
//            }

            return PagedModel.of(list, pagedModel.getMetadata());
//            return body;
        }

        if (body instanceof EntityModel<?> entityModel) {
//
            return EntityModel.of(Objects.requireNonNull(entityModel.getContent()));
//            return RepresentationModel.of(Objects.requireNonNull(entityModel.getContent()));
//            return  res.removeLinks();
//            return JSONObject.from(entityModel.getContent());
        }

        if (selectedContentType == MediaType.TEXT_HTML) {
            return body;
        }

        /// 防止重复包裹的问题出现

        return body;


    }
}
