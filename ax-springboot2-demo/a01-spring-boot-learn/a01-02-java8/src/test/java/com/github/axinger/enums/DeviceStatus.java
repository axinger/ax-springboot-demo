package com.github.axinger.enums;

enum DeviceSwitch implements DeviceType {
    OFF(0),
    ON(1),
    ;
    private final int code;

    DeviceSwitch(int code) {
        this.code = code;
    }

    @Override
    public int code() {
        return code;
    }
}


public enum DeviceStatus implements DeviceType {

    CLOSE(0),
    RUNNING(11, DeviceSwitch.ON),
    WAITING(12, DeviceSwitch.ON),
    WARNING(13, DeviceSwitch.ON),
    ;

    private final int code;

    private final DeviceSwitch type;

    DeviceStatus(int code) {
        this.code = code;
        this.type = DeviceSwitch.OFF;
    }

    DeviceStatus(int code, DeviceSwitch type) {
        this.code = code;
        this.type = type;
    }

    DeviceSwitch deviceSwitch() {
        return type;
    }

    @Override
    public int code() {
        return code;
    }

    // @Override
    // public String toString(){
    //     return code;
    // }
}

interface DeviceType {
    int code();
}


