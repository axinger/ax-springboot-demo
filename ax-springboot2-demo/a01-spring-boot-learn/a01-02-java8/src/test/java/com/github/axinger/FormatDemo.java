package com.github.axinger;

import java.text.DecimalFormat;

public class FormatDemo {
    // 此方法用于完成数字的格式化显示
    public void format1(String parrent, double value) {
        DecimalFormat decimalFormat = null;// 声明对象
        decimalFormat = new DecimalFormat(parrent);// 实例化并传入模板
        String string = decimalFormat.format(value);// 格式化数字
        System.out.println("string = " + string);
    }

}
