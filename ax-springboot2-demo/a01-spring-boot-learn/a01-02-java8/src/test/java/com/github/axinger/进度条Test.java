package com.github.axinger;

import lombok.SneakyThrows;
import me.tongfei.progressbar.ProgressBar;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class 进度条Test {

    @Test
    public void showColoredProgressBar() {
        int total = 100;
        for (int i = 0; i <= total; i++) {
            printColoredProgressBar(i, total);
            try {
                Thread.sleep(50); // 模拟处理时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printColoredProgressBar(int current, int total) {
        int width = 50; // 进度条宽度
        float percent = (float) current / total;
        int progress = (int) (width * percent);

        // ANSI颜色代码
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String reset = "\u001B[0m";

        StringBuilder sb = new StringBuilder("\r[");
        for (int i = 0; i < width; i++) {
            if (i < progress) {
                sb.append(green).append("█");
            } else if (i == progress) {
                sb.append(yellow).append("▶");
            } else {
                sb.append(reset).append(" ");
            }
        }
        sb.append(reset).append("] ");
        sb.append(String.format("%.1f", percent * 100)).append("% ");
        sb.append(current).append("/").append(total);

        System.out.print(sb.toString());

        if (current == total) {
            System.out.println();
        }
    }


    @SneakyThrows
    @Test
    public void showProgressWithLibrary() {

        CompletableFuture.runAsync(() -> {
            try (ProgressBar pb = new ProgressBar("处理进度:", 100)) {
                for (int i = 0; i < 102; i++) {
                    pb.step();
                    Thread.sleep(50); // 模拟处理时间
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("==================");

        TimeUnit.SECONDS.sleep(10);
    }
}
