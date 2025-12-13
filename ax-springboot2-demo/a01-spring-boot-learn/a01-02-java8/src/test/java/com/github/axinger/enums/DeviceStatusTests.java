package com.github.axinger.enums;


import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

public class DeviceStatusTests {

    /// 嵌套枚举
    @Test
    public void test1() {

        System.out.println("DeviceStatus.CLOSE.type() = " + DeviceStatus.CLOSE.code());
        System.out.println("DeviceStatus.CLOSE = " + DeviceStatus.CLOSE);
        System.out.println("DeviceStatus.CLOSE.getType().type() = " + DeviceStatus.CLOSE.deviceSwitch().code());

        System.out.println("DeviceStatus.RUNNING.type() = " + DeviceStatus.RUNNING.code());
        System.out.println("DeviceStatus.RUNNING.getCode() = " + DeviceStatus.RUNNING.deviceSwitch().code());
        System.out.println("DeviceStatus.RUNNING.name() = " + DeviceStatus.RUNNING.name());


        DeviceStatus status = !RandomUtil.randomBoolean() ? DeviceStatus.CLOSE : DeviceStatus.RUNNING;

        System.out.println("status = " + status);
        switch (status) {
            case CLOSE -> {
                switch (status.deviceSwitch()) {
                    case ON -> {
                        System.out.println("CLOSE-ON=============");
                    }
                    case OFF -> {
                        System.out.println("CLOSE-OFF=============2");
                    }
                }
            }
            case RUNNING -> {
                switch (status.deviceSwitch()) {
                    case ON -> {
                        System.out.println("RUNNING-ON=============2");
                    }
                    case OFF -> {
                        System.out.println("RUNNING-OFF=============2");
                    }
                }
            }
            default -> {
                System.out.println("!CLOSE=============");
            }
        }
    }


    @Test
    void test2() {


    }
}
