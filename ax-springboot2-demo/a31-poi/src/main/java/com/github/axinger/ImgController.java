package com.github.axinger;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class ImgController {

    @GetMapping(value = "/generate-image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateImage() throws IOException {
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // 设置字体和颜色
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 8));

        // 绘制标题（居中）
        String title = "Title";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        int titleX = (width - titleWidth) / 2;
        g2d.drawString(title, titleX, 10);

        // 绘制第二行文字（左对齐）
        String secondLine = "Left";
        g2d.drawString(secondLine, 2, 20);

        // 绘制第三行文字（右对齐）
        String thirdLine = "Right";
        int thirdLineWidth = g2d.getFontMetrics().stringWidth(thirdLine);
        int thirdLineX = width - thirdLineWidth - 2;
        g2d.drawString(thirdLine, thirdLineX, 30);

        g2d.dispose();

        // 将图像转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
