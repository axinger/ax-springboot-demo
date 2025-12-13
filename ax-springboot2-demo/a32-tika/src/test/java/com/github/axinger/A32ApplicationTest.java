package com.github.axinger;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class A32ApplicationTest {

    @Autowired
    private Tika tika;


    /**
     * 通过tika工具检测文件实际类型
     *
     * @throws IOException       IO异常
     * @throws MimeTypeException MimeType异常
     */
    @Test
    public void getMimeTypeTest() throws IOException, TikaException {
        // 获取文件
//        File gifFile = ResourceUtils.getFile("classpath:files/test.gif");
//        File jpgFile = ResourceUtils.getFile("classpath:files/test.jpg");
//        File pngFile = ResourceUtils.getFile("classpath:files/test.png");
//        File xlsFile = ResourceUtils.getFile("classpath:files/test.xls");
//        File xlsxFile = ResourceUtils.getFile("classpath:files/test.xlsx");
//        File docFile = ResourceUtils.getFile("classpath:files/test.doc");
        File docxFile = ResourceUtils.getFile("classpath:files/test.docx");
//        File sqlFile = ResourceUtils.getFile("classpath:files/test.sql");

        // 使用tika提供的外观工具,进行检测
        Tika tika = new Tika();
        // 此处检测文件内容,返回文件MimeType名称
        String detect = tika.detect(docxFile);
        System.out.println("MimeType:" + detect);

        // 获取tika提供的默认参照表
        // 可以进行自定义,参照https://stackoverflow.com/questions/13650372/how-to-determine-appropriate-file-extension-from-mime-type-in-java
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        // 根据MimeType名称获取MimeType类型
        MimeType mimeType = allTypes.forName(detect);
        // 根据MimeType类型获取对应的后缀名
        String extension = mimeType.getExtension();
        System.out.println("extension = " + extension);

        String s = tika.parseToString(docxFile);
        System.out.println("s = " + s);
    }

    @Test
    void contextLoads() throws TikaException, IOException {
        Path path = Paths.get("C:\\Users\\xing\\Desktop\\test.pdf");
        File file = path.toFile();

        String detect = tika.detect(file);
        System.out.println("文件类型:" + detect);


        String content = tika.parseToString(file);
        System.out.println("解析后的文章内容如下：\n" + content);

    }


    @Test
    void test3() throws TikaException, IOException {
        Path path = Paths.get("C:\\Users\\xing\\Desktop\\test.xlsx");
        File file = path.toFile();

        String detect = tika.detect(file);
        System.out.println("文件类型:" + detect);


        String content = tika.parseToString(file);
        System.out.println("解析后的文章内容如下：\n" + content);

    }

}
