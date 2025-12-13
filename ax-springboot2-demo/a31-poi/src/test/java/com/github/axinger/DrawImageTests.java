package com.github.axinger;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawImageTests {
    @Test
    void test1() {
        // 创建一个新的 BufferedImage 对象
        int width = 600; // 图像宽度
        int height = 400; // 图像高度
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取 Graphics2D 对象
        Graphics2D g2d = image.createGraphics();

        // 设置背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // 设置标题字体和颜色
        Font titleFont = new Font("宋体", Font.BOLD, 30); // 标题字体大小为30
        g2d.setFont(titleFont);
        g2d.setColor(Color.BLUE); // 标题颜色为蓝色

        // 绘制居中的标题
        String titleText = "Poi-tl 模板引擎";

        // 获取FontMetrics对象
        FontMetrics fontMetrics = g2d.getFontMetrics();

        int titleX = (width - fontMetrics.stringWidth(titleText)) / 2;
        int titleY = fontMetrics.getHeight() + 50; // 简单地将标题放在顶部中间位置
        g2d.drawString(titleText, titleX, titleY);

        // 设置普通文本字体和颜色
        g2d.setFont(new Font("宋体", Font.PLAIN, 20)); // 普通文本字体大小为20
        g2d.setColor(Color.RED);

        // 绘制边框
        int w1 = 500;
        int h1 = 300;
        g2d.drawRect(50, 50, w1, h1); // x, y, width, height

        // 定义文本内容
        String labelName = "姓名:张三";
        String labelGender = "性别:男";

        // 计算并设置姓名文本位置（左对齐）
        int textStartY = 250; // 起始y坐标
        g2d.drawString(labelName, 0, textStartY);


        // 要测量宽度的字符串
        String text = "这是一个测试字符串";

        // 计算字符串的像素宽度
        int textWidth = fontMetrics.stringWidth(text);
        System.out.println("textWidth = " + textWidth);

        g2d.drawString(labelGender, w1 - textWidth - 50, textStartY);


        // 关闭图形上下文
        g2d.dispose();

        // 保存图像到文件
        try {
            File outputfile = new File("output.png");
            ImageIO.write(image, "png", outputfile);
            System.out.println("Image saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
