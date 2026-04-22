package com.axing.common.util.utils;

import com.axing.common.response.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResponseUtil {

    @SneakyThrows
    public static <T> void writeFail(HttpServletResponse response, Result<T> result) {
        write(response, HttpServletResponse.SC_UNAUTHORIZED, result);
    }

    @SneakyThrows
    public static <T> void writeSuccess(HttpServletResponse response, Result<T> result) {
        write(response, HttpServletResponse.SC_OK, result);
    }

    @SneakyThrows
    public static <T> void write(HttpServletResponse response, int status, Result<T> result) {
        // 关键：在获取Writer之前设置字符编码和内容类型
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);

        // 使用try-with-resources确保资源正确关闭
        try (PrintWriter out = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            out.write(objectMapper.writeValueAsString(result));
            out.flush();
        }
    }


}
