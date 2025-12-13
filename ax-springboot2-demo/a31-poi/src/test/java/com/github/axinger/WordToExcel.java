package com.github.axinger;


import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class WordToExcel {


    @SneakyThrows
    @Test
    public void test1() {
        // 加载Word文档
        XWPFDocument doc = new XWPFDocument(new FileInputStream("D:\\Users\\cepai\\Desktop\\123.docx"));

        // 创建Excel文档
        HSSFWorkbook wb = new HSSFWorkbook();
        FileOutputStream out = new FileOutputStream("D:\\Users\\cepai\\Desktop\\123.xls");

        int sheetIndex = 0;

        // 遍历Word中的表格
        for (XWPFTable table : doc.getTables()) {
            // 创建Excel中的sheet
            wb.createSheet("Sheet" + (++sheetIndex));
            // 获取sheet对象
            HSSFSheet sheet = wb.getSheetAt(sheetIndex - 1);
            int rowIndex = 0;
            // 遍历表格中的行
            for (XWPFTableRow row : table.getRows()) {
                // 在Excel中创建行
                HSSFRow hssfRow = sheet.createRow(rowIndex++);
                int columnIndex = 0;
                // 遍历行中的单元格
                // 遍历行中的单元格
                for (XWPFTableCell cell : row.getTableCells()) {
                    // 在Excel中创建单元格
                    HSSFCell hssfCell = hssfRow.createCell(columnIndex++);
                    // 获取单元格文本内容
                    String content = cell.getText();
                    // 设置单元格的值
                    hssfCell.setCellValue(content);

                    // 处理单元格中的图片
                    for (IBodyElement elem : cell.getBodyElements()) {
                        if (elem instanceof XWPFParagraph) {
                            List<XWPFRun> runs = ((XWPFParagraph) elem).getRuns();
                            for (XWPFRun run : runs) {
                                for (XWPFPicture picture : run.getEmbeddedPictures()) {
                                    // 将图片转换为Excel中的图片，并插入到对应的单元格中
                                    byte[] data = picture.getPictureData().getData();
                                    int pictureIndex = wb.addPicture(data, Workbook.PICTURE_TYPE_PNG);
                                    CreationHelper helper = wb.getCreationHelper();
                                    Drawing<?> drawing = sheet.createDrawingPatriarch();
                                    ClientAnchor anchor = helper.createClientAnchor();
                                    anchor.setCol1(hssfCell.getColumnIndex());
                                    anchor.setCol2(hssfCell.getColumnIndex() + 1);
                                    anchor.setRow1(hssfCell.getRowIndex());
                                    anchor.setRow2(hssfCell.getRowIndex() + 1);
                                    // 将图片插入到单元格，并设定位置
                                    Picture pic = drawing.createPicture(anchor, pictureIndex);
                                    int dx = Units.toEMU(10); // 水平方向偏移量
                                    int dy = Units.toEMU(-20); // 垂直方向偏移量
                                    anchor.setDx1(dx); // 设置起始点X轴偏移量
                                    anchor.setDy1(dy); // 设置起始点Y轴偏移量
                                    anchor.setDx2(dx); // 设置结束点X轴偏移量
                                    anchor.setDy2(dy); // 设置结束点Y轴偏移量
                                }
                            }
                        }
                    }
                }

            }
        }

        // 将Excel文档保存到文件系统中
        wb.write(out);
        out.close();
        wb.close();

        System.out.println("Excel file has been generated.");
    }
}
