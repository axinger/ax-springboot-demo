package com.github.axinger.service;

import cn.hutool.core.io.FileUtil;
import com.github.axinger.model.TimePaperPojo;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DrawImgService {


    @SneakyThrows
    public String drawTimePaperImg(TimePaperPojo dto) {
        int imgWidth = 600;
        int imgHeight = 600;

        // 创建一个新的BufferedImage对象
        BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        // 获取Graphics2D对象
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /// 解决linux没有中文字体,使用自定义字体
//        Resource resource = new ClassPathResource("/fonts/SourceHanSerifSC-SemiBold.otf");
//        Resource resource = new ClassPathResource("/fonts/SourceHanSerifSC-VF.ttf");
        Resource resource = new ClassPathResource("fonts/SourceHanSerifSC-Bold.otf");
//        Resource resource = new ClassPathResource("simfang.ttf");
//        Resource resource = new ClassPathResource("simhei.ttf");

        @Cleanup InputStream fontStream = resource.getInputStream();

        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(30f);

        // 设置字体和颜色
//        Font font = new Font(null, Font.BOLD, 25); // 使用系统默认字体

        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        // 设置背景颜色为白色
        g2d.setBackground(Color.PINK);
        g2d.clearRect(0, 0, image.getWidth(), image.getHeight());

        // 绘制文本
        int y = 100; // 初始y值
        int space = 10;

        int margin = 100;
        int maxWidth = imgWidth - 2 * margin;

        FontMetrics fontMetrics = g2d.getFontMetrics(font);

        // 计算并绘制每行文本
        g2d.drawString(dto.getBestTime(), margin, y);

        y += fontMetrics.getHeight();
//        g2d.drawString(dto.getBestTimeContent(), margin, y);

        /// 1.先分割字符串
        List<String> strings = splitIntoLines(dto.getBestTimeContent(), fontMetrics, maxWidth);

        /// 2.分割后的字符串,再绘制,自定定义间距
        for (String line : strings) {
            g2d.drawString(line, margin, y);
            y += fontMetrics.getHeight(); /// 这是最后一个,会多一个间隙
        }

        y += fontMetrics.getHeight() + space;

        g2d.drawString(dto.getAvailableTimeTitle(), margin, y);
        y += fontMetrics.getHeight();

        g2d.drawString(dto.getAvailableTimeContent(), margin, y);
        y += fontMetrics.getHeight() + space;

        // 居中文本
        String centeredText = dto.getProductName();
        int textWidth = fontMetrics.stringWidth(centeredText);
        int x = (image.getWidth() - textWidth) / 2;
        g2d.drawString(centeredText, x, y + space);
        y += fontMetrics.getHeight() + space * 2;

        g2d.drawString(dto.getArea(), margin, y);
        y += fontMetrics.getHeight() + space;

        g2d.drawString(dto.getInstructionsTitle(), margin, y);
        y += fontMetrics.getHeight() + space;


        File file = FileUtil.touch(FileUtil.getTmpDir() + "/test.png");


        ImageIO.write(image, "png", file);

        return file.getAbsolutePath();
    }

    /// 最大宽度,分割字符串,避免超出最大宽度
    private List<String> splitIntoLines(String text, FontMetrics fm, int maxWidth) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;

        int lineStart = 0;
        int currentPos = 1;

        while (currentPos <= text.length()) {
            String testLine = text.substring(lineStart, currentPos);
            int testWidth = fm.stringWidth(testLine);

            if (testWidth > maxWidth) {
                // 需要换行
                if (currentPos == lineStart + 1) {
                    // 单个字符就超过宽度，强制换行
                    lines.add(testLine);
                    lineStart = currentPos;
                } else {
                    // 回退到前一个字符
                    lines.add(text.substring(lineStart, currentPos - 1));
                    lineStart = currentPos - 1;
                }
            }
            currentPos++;
        }
        // 添加最后一行
        if (lineStart < text.length()) {
            lines.add(text.substring(lineStart));
        }
        return lines;
    }
}
