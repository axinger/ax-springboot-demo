package com.github.axinger.domain;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MachineStatusEnum {

    /**
     * online bad   -1 offline
     * online good
     * online 0: 关机 0  shutdown
     * online 1, alarm 1 : 故障 11 breakdown
     * online 1, alarm 0, running:0  : 待机 100  await
     * online 1, alarm 0, running:1  : 运行 101 run
     */
    OFF("-1", "离线", "0"), // -1
    // 关机 online=0
    SHUTDOWN("0", "关机", "0"), // 0
    // 开机有警告,忽略运行: 为故障
    BREAKDOWN("1", "故障", "0"), // 11 -> 1
    // 开机无警告,但未运行 : 为待机
    STANDBY("2", "待机", "0"), // 100 -> 2
    // 开机无警告,且运行: 为运行
    RUNNING("3", "运行", "1"), // 101 -> 3

    ;


    @JsonValue
    @EnumValue
    private final String code;
    private final String alias;
    private final String value;


    public static MachineStatusEnum ofCode(String code) {
        MachineStatusEnum[] values = MachineStatusEnum.values();
        for (MachineStatusEnum me : values) {
            if (me.code.equals(code)) {
                return me;
            }
        }
        return OFF;
    }

    /**
     * 设备状态
     *
     * @param machinesOnline
     * @param alarm
     * @param running
     * @return
     */
//    public static MachineStatusEnum ofStatus(String machinesOnline,
//                                             String alarm,
//                                             String running) {
//
//        machinesOnline = MachineStatusEnum.ofSafe(Optional.ofNullable(machinesOnline).orElse("0")).getCode();
//        alarm = StatusEnum.ofSafe(Optional.ofNullable(alarm).orElse("0")).getCode();
//        running = StatusEnum.ofSafe(Optional.ofNullable(running).orElse("1")).getCode();
//
//
//        // 设备开关状态优先级高于运行状态，关机的情况下不处理运行状态
//
//        // machinesOnline 有值
//        // 运行: true - > 在判断其他请情况
//        // 运行: false - > abnormal 停机
//        // 关机 online=0
//        if (machinesOnline.equals(StatusEnum.abnormal.getCode())) {
//            return MachineStatusEnum.OFF;
//        }
//
//        // 报警有值
//        // 报警: true normal - > 故障
//        // 报警: false - > 忽略
//        // 开机,有警告,忽略运行
//        if (alarm.equals(StatusEnum.normal.getCode())) {
//            return MachineStatusEnum.BREAKDOWN;
//        }
//
//        // 运行有值
//        // 运行: true - normal > 正常
//        // 运行: false - > 待机
//        // 开机无警告,但未运行
//        if (running.equals(StatusEnum.normal.getCode())) {
//            return MachineStatusEnum.RUNNING;
//        }
//
//        // 开机无警告,但未运行
//        if (running.equals(StatusEnum.abnormal.getCode())) {
//            return MachineStatusEnum.RUNNING;
//        }
//        return MachineStatusEnum.SHUTDOWN;
//    }
    public static MachineStatusEnum of(Integer machinesOnline,
                                       Integer alarm,
                                       Integer running) {

        if (ObjUtil.equal(machinesOnline, -1) ||
                ObjUtil.equal(alarm, -1) ||
                ObjUtil.equal(running, -1)) {
            return MachineStatusEnum.OFF;
        }

        if (ObjUtil.isNull(machinesOnline)) {
            return MachineStatusEnum.OFF;
        }

        if (ObjUtil.isNull(alarm)) {
            return MachineStatusEnum.OFF;
        }
        if (ObjUtil.isNull(running)) {
            return MachineStatusEnum.OFF;
        }

        machinesOnline = machinesOnline > 0 ? 1 : 0;
        alarm = alarm > 0 ? 1 : 0;
        running = running > 0 ? 1 : 0;

        if (ObjUtil.equal(machinesOnline, 0)) {
            return MachineStatusEnum.SHUTDOWN;
        }

        if (ObjUtil.equal(alarm, 1)) {
            return MachineStatusEnum.BREAKDOWN;
        }

        if (ObjUtil.equal(running, 0)) {
            return MachineStatusEnum.STANDBY;
        }

        if (ObjUtil.equal(running, 1)) {
            return MachineStatusEnum.RUNNING;
        }
        return MachineStatusEnum.OFF;
    }

    @Override
    public String toString() {
        return alias;
    }
}
