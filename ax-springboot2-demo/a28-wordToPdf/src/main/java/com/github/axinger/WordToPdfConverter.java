package com.github.axinger;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class WordToPdfConverter {

    /**
     * 使用 fr.opensagres.poi.xwpf.converter.pdf 包
     *
     * @param sourceFileName
     * @param destinationFileName
     * @throws IOException
     */
    public static void convert(String sourceFileName,
                               String destinationFileName) throws IOException {

        File sourceFile = new File(sourceFileName);
        InputStream inputStream = new FileInputStream(sourceFile);
        XWPFDocument document = new XWPFDocument(inputStream);

        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(destinationFileName);
        PdfConverter.getInstance().convert(document, out, options);

        inputStream.close();
        document.close();
        out.flush(); // 刷新缓冲区
        out.close(); // 关闭流
    }
}
