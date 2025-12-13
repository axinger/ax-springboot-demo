package com.axing.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.axing.common.response.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author xing
 * @version 1.0.0
 */

@Slf4j
public class ExcelUtils {

    /**
     * 读取excel
     *
     * @param file         file
     * @param excelClass   excelClass
     * @param readListener readListener
     * @param <T>          T
     */
    public static <T> void readExcel(MultipartFile file,
                                     Class<T> excelClass,
                                     ReadListener<T> readListener) {

        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            String msg = String.format("excel,请求文件流异常 %s", e.getMessage());
            log.error(msg);
            throw ServiceException.message(msg);
        }

        EasyExcel.read(inputStream, excelClass, readListener)
                .sheet()
                .doRead();
    }


    public static void writeExcel(HttpServletResponse response,
                                  Class excelClass,
                                  String fileName,
                                  String sheetName,
                                  Collection<?> data) {
        OutputStream outputStream = excelStream(response, fileName, ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(outputStream, excelClass)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(data == null ? "Sheet1" : sheetName)
                .registerWriteHandler(ExcelUtils.horizontalCellStyleStrategy())
                .doWrite(data == null ? new ArrayList<>() : data);
    }

    /**
     * excel文件流
     *
     * @param response HttpServletResponse
     * @param fileName 文件名
     * @param suffix   文件后缀名,包含.
     */
    public static OutputStream excelStream(HttpServletResponse response, String fileName, String suffix) {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + suffix);

        try {
            return response.getOutputStream();
        } catch (IOException e) {
            String msg = String.format("excel getOutputStream 失败 = %s", e.getMessage());
            log.error(msg);
            throw ServiceException.message(msg);
        }
    }


    /**
     * excel 表头样式
     *
     * @return
     */
    public static HorizontalCellStyleStrategy horizontalCellStyleStrategy() {

        /// 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为灰色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        // 字体样式
//        headWriteFont.setFontName("Frozen");
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 自动换行
        headWriteCellStyle.setWrapped(false);
        // 水平对齐方式
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 垂直对齐方式
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SQUARES);
        // 背景白色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 12);
        // 字体样式
//        contentWriteFont.setFontName("Calibri");
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy strategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return strategy;
    }
}
