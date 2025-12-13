package com.github.axinger.util;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Date: 2021/7/28 10:14
 */
public class StreamExportUtil<T> {

    static ServletOutputStream out = null;

    public static <T> void download(String fileName, CommonResultHandler<T> resultHandler) throws IOException {
        try {
            init(fileName, resultHandler.getResponse());
            resultHandler.getWriter().finish();
            out.flush();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void init(String fileName, HttpServletResponse response) throws IOException {
        out = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes("gb2312"), "ISO-8859-1") + ".xlsx");
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
    }
}
