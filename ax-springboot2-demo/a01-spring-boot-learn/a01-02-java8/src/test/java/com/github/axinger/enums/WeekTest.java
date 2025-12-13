package com.github.axinger.enums;

public class WeekTest {
    public static void main(String[] args) {
        Week.MONDAY.print();
        Week.TUESDAY.print();
        System.out.println("Week.TUESDAY.isWeekEnd() = " + Week.TUESDAY.isWeekEnd());
        System.out.println("Week.SATURDAY.isWeekEnd() = " + Week.SATURDAY.isWeekEnd());
    }
}
