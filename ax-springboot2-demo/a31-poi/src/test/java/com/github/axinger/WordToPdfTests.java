package com.github.axinger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WordToPdfTests {

    ///  读取word文字内容,写入pdf, 不能完成样式直接转化
    @Test
    void test1() throws IOException, DocumentException {

        // 加载Word文件
        ClassPathResource resource = new ClassPathResource("file/test01.docx");
        InputStream fis = resource.getInputStream();

        XWPFDocument document = new XWPFDocument(fis);
        // 创建PDF文档
        Document pdfDocument = new Document();
        PdfWriter.getInstance(pdfDocument, new FileOutputStream("output.pdf"));
        pdfDocument.open();


        // 读取Word文件内容
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String content = extractor.getText();

        // 将Word内容写入PDF文档
        Paragraph paragraph = new Paragraph(content);
        pdfDocument.add(paragraph);


        // 保存PDF文档
        pdfDocument.close();

        fis.close();

    }

    @Test
    void test2() throws IOException, DocumentException {
        // 加载Word文件
        ClassPathResource resource = new ClassPathResource("file/test01.docx");
        InputStream fis = resource.getInputStream();

        XWPFDocument document = new XWPFDocument(fis);

        // 创建PDF文档
        Document pdfDocument = new Document();
        PdfWriter.getInstance(pdfDocument, new FileOutputStream("output.pdf"));
        pdfDocument.open();

        // 处理Word文档中的段落
        for (XWPFParagraph para : document.getParagraphs()) {
            String text = para.getText();
            if (text != null && !text.trim().isEmpty()) {
                pdfDocument.add(new Paragraph(text)); // 将段落内容写入PDF
            }
        }

        // 处理Word文档中的表格
        for (XWPFTable table : document.getTables()) {
            int numColumns = table.getRow(0).getTableCells().size(); // 获取表格列数
            PdfPTable pdfTable = new PdfPTable(numColumns); // 创建PDF表格

            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    String cellText = cell.getText(); // 获取单元格内容
                    pdfTable.addCell(cellText); // 将单元格内容添加到PDF表格
                }
            }

            pdfDocument.add(pdfTable); // 将表格添加到PDF文档
        }

        // 保存PDF文档
        pdfDocument.close();
        fis.close();
    }
}
