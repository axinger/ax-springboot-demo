package com.github.axinger;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;

public class PdfToWordTests {
    @SneakyThrows
    @Test
    void test1() {
        /// itextpdf 库, 读取pdf
        // load PDF document
        PdfReader pdfReader = new PdfReader("D:\\Users\\cepai\\Desktop\\123.pdf");
        // extract text from PDF document
        String text = PdfTextExtractor.getTextFromPage(pdfReader, 1);

        // create Word document
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        // set text to Word document
        run.setText(text);

        // save Word document
        FileOutputStream out = new FileOutputStream("D:\\Users\\cepai\\Desktop\\123_2.docx");
        document.write(out);
        out.close();
        document.close();
    }
}
