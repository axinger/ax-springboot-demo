package com.github.axinger;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.BorderStyle;
import com.github.axinger.model.Student;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*
<dependency>
    <groupId>com.deepoove</groupId>
    <artifactId>poi-tl</artifactId>
</dependency>
 */
class PoiTlTests {


    /*

    {
  "text": "Sayi",
  "style": {
    "strike": false,
    "bold": true,
    "italic": false,
    "color": "00FF00",
    "underLine": false,
    "fontFamily": "微软雅黑",
    "fontSize": 12,
    "highlightColor": "green",
    "vertAlign": "superscript",
    "characterSpacing" : 20
  }
}
删除线
粗体
斜体
颜色
下划线
字体
字号
背景高亮色
上标或者下标
间距
     */
    @SneakyThrows
    @Test
    void test1() {
        ClassPathResource resource = new ClassPathResource("file/test01.docx");
        ClassPathResource logo = new ClassPathResource("file/logo.jpg");
        try (InputStream inputStream = resource.getInputStream();
             InputStream logoStream = logo.getInputStream()) {
            // 一行代码,写入占位符
            Map<Object, Object> data = new HashMap<>() {{

                put("title", Texts.of("Texts样式")
                        .color("000000")
                        .bold()
                        .fontFamily("微软雅黑")
                        .fontSize(12)
                        .create());
                put("name", "张三");
                put("sex", "男");

                // 指定图片路径
//                put("image", "logo.png");
//                // svg图片
//                put("svg", "https://img.shields.io/badge/jdk-1.6%2B-orange.svg");
//
//                // 图片文件
//                put("image1", Pictures.ofLocal("logo.png").size(120, 120).create());
//
                // 图片流
                put("streamImg", Pictures.ofStream(logoStream, PictureType.JPEG)
                        .size(100, 100)
                        .altMeta("图片不存在")
                        .create());

                // java图片，我们可以利用Java生成图表插入到word文档中
                // 将内容渲染为图片
//                BufferedImage buffered = renderTextToImage("文字渲染图片", 100, 100); // 图片尺寸

                BufferedImage buffered = ImgUtil.createImage("文字渲染图片",
                        new Font(null, Font.PLAIN, 12),
                        Color.RED,
                        Color.WHITE,
                        BufferedImage.TYPE_INT_RGB
                );
                put("buffered", Pictures.ofBufferedImage(buffered, PictureType.PNG)
                        .size(100, 100)
                        .altMeta("图片不存在")
                        .create());


                // 一个2行2列的表格
                put("table0", Tables.of(new String[][]{
                        new String[]{"00", "01"},
                        new String[]{"10", "11"}
                }).border(BorderStyle.DEFAULT).create());


                {
                    // 第0行居中且背景为蓝色的表格
                    RowRenderData row0 = Rows.of("姓名", "学历").textColor("FFFFFF")
                            .bgColor("4472C4").center().create();
                    RowRenderData row1 = Rows.create("李四", "博士");
                    put("table1", Tables.create(row0, row1));
                }
                {

                    // 合并第1行所有单元格的表格
                    RowRenderData row0 = Rows.of("列0", "列1", "列2").center().bgColor("4472C4").create();
                    RowRenderData row1 = Rows.create("没有数据", null, null);
                    MergeCellRule rule = MergeCellRule.builder().map(MergeCellRule.Grid.of(1, 0), MergeCellRule.Grid.of(1, 2)).build();
                    put("table3", Tables.of(row0, row1).mergeRule(rule).create());
                }

                /// 编号样式支持罗马字符、有序无序等，可以通过 Numberings.of(NumberingFormat) 来指定。
                put("list", Numberings
                                .of(NumberingFormat.BULLET)
                                .addItem("第一个")
                                .addItem("第一个")
                                .addItem("第一个")
                                .addItem("第一个")
                                .create()
//                        .create("Plug-in grammar",
//                        "Supports word text, pictures, table...",
//                        "Not just templates")

                );

                //引用图片标签是一个文本
//                put("img", Pictures.ofLocal("sayi.png").create());


                /// 多系列图表指的是条形图（3D条形图）、柱形图（3D柱形图）、面积图（3D面积图）、折线图（3D折线图）、雷达图、散点图等。
                ChartMultiSeriesRenderData chart = Charts
                        .ofMultiSeries("ChartTitle", new String[]{"中文", "English"})
                        .addSeries("countries", new Double[]{15.0, 6.0})
                        .addSeries("speakers", new Double[]{223.0, 119.0})
                        .create();

                put("barChart", chart);

            }};

            File file = FileUtil.file("/opt/temp/a31-poi/test01.docx");
            FileUtil.del(file);
            XWPFTemplate template = XWPFTemplate.compile(inputStream).render(data);
            String path = file.getPath();
            template.writeToFile(path);
            System.out.println("path = " + path);
        }
    }

    @Test
    void test1_自定义对象() throws IOException {
        ClassPathResource resource = new ClassPathResource("file/test01.docx");
        try (InputStream inputStream = resource.getInputStream()) {
            // 一行代码,写入占位符
            Student student = new Student();
            student.setTitle("自定义对象");
            student.setName("张三");
            student.setAge(18);
            student.setSex("男");
            File file = FileUtil.file("/opt/temp/a31-poi/test1_自定义对象.docx");
            FileUtil.del(file);
            XWPFTemplate template = XWPFTemplate.compile(inputStream).render(student);
            String path = file.getPath();
            template.writeToFile(path);
            System.out.println("path = " + path);
        }
    }


    @SneakyThrows
    @Test
    void test3_table() {

    }


}
