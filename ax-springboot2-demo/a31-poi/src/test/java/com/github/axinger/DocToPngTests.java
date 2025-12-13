package com.github.axinger;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DocToPngTests {


    @Test
    void test1() {


        ClassPathResource resource = new ClassPathResource("file/test02.docx");

        try (InputStream docxStream = resource.getInputStream()) {
//            String docxFilePath = "input.docx"; // 输入的 .docx 文件路径

            String pdfFilePath = "output.pdf"; // 中间生成的 PDF 文件路径
            String outputImagePath = "output.png"; // 输出的 PNG 图片路径


            // 1. 将 .docx 转换为 PDF
            convertDocxToPdf(docxStream, pdfFilePath);

            // 2. 将 PDF 转换为 PNG
            convertPdfToPng(pdfFilePath, outputImagePath);

            System.out.println("图片生成成功：" + outputImagePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将 .docx 文件转换为 PDF
     *
     * @param fis         输入的 .docx 文件路径
     * @param pdfFilePath 输出的 PDF 文件路径
     */
    private void convertDocxToPdf(InputStream fis, String pdfFilePath) throws IOException {
        // 读取 .docx 文件
//        FileInputStream fis = new FileInputStream(docxFilePath);
        XWPFDocument document = new XWPFDocument(fis);

        // 创建 PDF 文档
        PDDocument pdfDocument = new PDDocument();
        PDPage page = new PDPage();
        pdfDocument.addPage(page);

        // 写入内容到 PDF
        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750); // 设置起始位置

        // 提取 .docx 内容并写入 PDF
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            for (XWPFRun run : paragraph.getRuns()) {
                contentStream.showText(run.getText(0));
            }
            contentStream.newLineAtOffset(0, -15); // 换行
        }

        contentStream.endText();
        contentStream.close();

        // 保存 PDF 文件
        pdfDocument.save(pdfFilePath);
        pdfDocument.close();
        document.close();
    }

    /**
     * 将 PDF 文件转换为 PNG 图片
     *
     * @param pdfFilePath     输入的 PDF 文件路径
     * @param outputImagePath 输出的 PNG 图片路径
     */
    private void convertPdfToPng(String pdfFilePath, String outputImagePath) throws IOException {
        // 加载 PDF 文件
        PDDocument document = PDDocument.load(new File(pdfFilePath));
        PDFRenderer renderer = new PDFRenderer(document);

        // 渲染第一页为图片
        BufferedImage image = renderer.renderImageWithDPI(0, 300); // 300 DPI

        // 保存为 PNG 文件
        ImageIO.write(image, "png", new File(outputImagePath));

        document.close();
    }

}
