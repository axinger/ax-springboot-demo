package com.github.axinger;

import com.deepoove.poi.XWPFTemplate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class A28WordToPdfApplicationTest {

    String wordPath = "D:\\test01.docx";

    String pdfPath = "D:\\123.pdf";

    @SneakyThrows
    @Test
    void test1() {
        /// 负责的效果不好
        WordToPdfConverter.convert(wordPath,
                pdfPath);
        TimeUnit.SECONDS.sleep(5);
    }


    @SneakyThrows
    @Test
    void test2() {
        /// 负责的效果好
        Word2PdfUtil.doc2Pdf(wordPath,
                pdfPath);
        TimeUnit.SECONDS.sleep(5);
    }

    @SneakyThrows
    @Test
    void test3() {

        Map<String, Object> datas = new HashMap<>();
        datas.put("title", "我是活动标题");
        XWPFTemplate template = XWPFTemplate.compile("/Users/xing/Desktop/test.docx");
        template.render(datas);
        FileOutputStream out = new FileOutputStream("/Users/xing/Desktop/test1.docx");
        template.write(out);
        template.close();
        out.close();


//        Map<String,Object> datas = new HashMap<>();
//        datas.put("title", "我是活动标题"); //给{{title}}标识赋值
//        XWPFTemplate template = XWPFTemplate.compile("/Users/xing/Desktop/test.docx");
//        template.render(datas);
////将word转成pdf
//        PdfOptions options = PdfOptions.create();
//        OutputStream outPDF = Files.newOutputStream(Paths.get("/Users/xing/Desktop/tes2.pdf"));
//
//        try  {
//            PdfConverter.getInstance().convert(template.getXWPFDocument(), outPDF, options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//
//            template.close();
//            outPDF.close();
//            outPDF.flush(); // 刷新缓冲区
//            outPDF.close(); // 关闭流
//        }

    }
}
